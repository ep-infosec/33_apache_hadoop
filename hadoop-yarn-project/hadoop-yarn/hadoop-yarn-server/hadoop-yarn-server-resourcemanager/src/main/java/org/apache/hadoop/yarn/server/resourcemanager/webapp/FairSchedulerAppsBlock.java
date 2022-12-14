/**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.apache.hadoop.yarn.server.resourcemanager.webapp;

import static org.apache.hadoop.yarn.util.StringHelper.join;
import static org.apache.hadoop.yarn.webapp.YarnWebParams.APP_STATE;
import static org.apache.hadoop.yarn.webapp.view.JQueryUI.C_PROGRESSBAR;
import static org.apache.hadoop.yarn.webapp.view.JQueryUI.C_PROGRESSBAR_VALUE;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.yarn.api.records.ApplicationAccessType;
import org.apache.hadoop.yarn.api.records.ApplicationAttemptId;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.QueueACL;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.server.resourcemanager.ResourceManager;
import org.apache.hadoop.yarn.server.resourcemanager.rmapp.RMApp;
import org.apache.hadoop.yarn.server.resourcemanager.rmapp.RMAppState;
import org.apache.hadoop.yarn.server.resourcemanager.scheduler.fair.FairScheduler;
import org.apache.hadoop.yarn.server.resourcemanager.webapp.dao.AppInfo;
import org.apache.hadoop.yarn.server.resourcemanager.webapp.dao.FairSchedulerInfo;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet.TABLE;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet.TBODY;
import org.apache.hadoop.yarn.webapp.util.WebAppUtils;
import org.apache.hadoop.yarn.webapp.view.HtmlBlock;

import com.google.inject.Inject;

import javax.servlet.http.HttpServletRequest;

/**
 * Shows application information specific to the fair
 * scheduler as part of the fair scheduler page.
 */
public class FairSchedulerAppsBlock extends HtmlBlock {
  final ConcurrentMap<ApplicationId, RMApp> apps;
  final FairSchedulerInfo fsinfo;
  final Configuration conf;
  final ResourceManager rm;
  final boolean filterAppsByUser;

  @Inject
  public FairSchedulerAppsBlock(ResourceManager rm, ViewContext ctx,
      Configuration conf) {
    super(ctx);
    this.conf = conf;
    this.rm = rm;

    this.filterAppsByUser  = conf.getBoolean(
        YarnConfiguration.FILTER_ENTITY_LIST_BY_USER,
        YarnConfiguration.DEFAULT_DISPLAY_APPS_FOR_LOGGED_IN_USER);

    FairScheduler scheduler = (FairScheduler) rm.getResourceScheduler();
    fsinfo = new FairSchedulerInfo(scheduler);
    apps = new ConcurrentHashMap<ApplicationId, RMApp>();
    for (Map.Entry<ApplicationId, RMApp> entry : rm.getRMContext().getRMApps()
        .entrySet()) {
      if (!(RMAppState.NEW.equals(entry.getValue().getState())
          || RMAppState.NEW_SAVING.equals(entry.getValue().getState())
          || RMAppState.SUBMITTED.equals(entry.getValue().getState()))) {
        if (!filterAppsByUser || hasAccess(entry.getValue(),
            ctx.requestContext().getRequest())) {
          apps.put(entry.getKey(), entry.getValue());
        }
      }
    }
  }

  private UserGroupInformation getCallerUserGroupInformation(
      HttpServletRequest hsr, boolean usePrincipal) {
    String remoteUser = hsr.getRemoteUser();
    if (usePrincipal) {
      Principal princ = hsr.getUserPrincipal();
      remoteUser = princ == null ? null : princ.getName();
    }

    UserGroupInformation callerUGI = null;
    if (remoteUser != null) {
      callerUGI = UserGroupInformation.createRemoteUser(remoteUser);
    }

    return callerUGI;
  }

  protected Boolean hasAccess(RMApp app, HttpServletRequest hsr) {
    // Check for the authorization.
    UserGroupInformation callerUGI = getCallerUserGroupInformation(hsr, true);
    List<String> forwardedAddresses = null;
    String forwardedFor = hsr.getHeader(RMWSConsts.FORWARDED_FOR);
    if (forwardedFor != null) {
      forwardedAddresses = Arrays.asList(forwardedFor.split(","));
    }

    if (callerUGI != null
        && !(this.rm.getApplicationACLsManager().checkAccess(callerUGI,
        ApplicationAccessType.VIEW_APP, app.getUser(),
        app.getApplicationId())
        || this.rm.getQueueACLsManager().checkAccess(callerUGI,
        QueueACL.ADMINISTER_QUEUE, app, hsr.getRemoteAddr(),
        forwardedAddresses))) {
      return false;
    }
    return true;
  }

  private static String printAppInfo(long value) {
    if (value == -1) {
      return "N/A";
    }
    return String.valueOf(value);
  }

  @Override public void render(Block html) {
    TBODY<TABLE<Hamlet>> tbody = html.
      table("#apps").
        thead().
          tr().
            th(".id", "ID").
            th(".user", "User").
            th(".name", "Name").
            th(".type", "Application Type").
            th(".queue", "Queue").
            th(".fairshare", "Fair Share").
            th(".starttime", "StartTime").
            th(".launchTime", "LaunchTime").
            th(".finishtime", "FinishTime").
            th(".state", "State").
            th(".finalstatus", "FinalStatus").
            th(".runningcontainer", "Running Containers").
            th(".allocatedCpu", "Allocated CPU VCores").
            th(".allocatedMemory", "Allocated Memory MB").
            th(".reservedCpu", "Reserved CPU VCores").
            th(".reservedMemory", "Reserved Memory MB").
            th(".progress", "Progress").
            th(".ui", "Tracking UI").__().__().
        tbody();
    Collection<YarnApplicationState> reqAppStates = null;
    String reqStateString = $(APP_STATE);
    if (reqStateString != null && !reqStateString.isEmpty()) {
      String[] appStateStrings = reqStateString.split(",");
      reqAppStates = new HashSet<YarnApplicationState>(appStateStrings.length);
      for(String stateString : appStateStrings) {
        reqAppStates.add(YarnApplicationState.valueOf(stateString));
      }
    }
    StringBuilder appsTableData = new StringBuilder("[\n");
    for (RMApp app : apps.values()) {
      if (reqAppStates != null && !reqAppStates.contains(app.createApplicationState())) {
        continue;
      }
      AppInfo appInfo = new AppInfo(rm, app, true, WebAppUtils.getHttpSchemePrefix(conf));
      String percent = StringUtils.format("%.1f", appInfo.getProgress());
      ApplicationAttemptId attemptId = app.getCurrentAppAttempt().getAppAttemptId();
      long fairShare = fsinfo.getAppFairShare(attemptId);
      if (fairShare == FairSchedulerInfo.INVALID_FAIR_SHARE) {
        // FairScheduler#applications don't have the entry. Skip it.
        continue;
      }
      appsTableData.append("[\"<a href='")
      .append(url("app", appInfo.getAppId())).append("'>")
      .append(appInfo.getAppId()).append("</a>\",\"")
      .append(StringEscapeUtils.escapeEcmaScript(StringEscapeUtils.escapeHtml4(
        appInfo.getUser()))).append("\",\"")
      .append(StringEscapeUtils.escapeEcmaScript(StringEscapeUtils.escapeHtml4(
        appInfo.getName()))).append("\",\"")
      .append(StringEscapeUtils.escapeEcmaScript(StringEscapeUtils.escapeHtml4(
        appInfo.getApplicationType()))).append("\",\"")
      .append(StringEscapeUtils.escapeEcmaScript(StringEscapeUtils.escapeHtml4(
        appInfo.getQueue()))).append("\",\"")
      .append(fairShare).append("\",\"")
      .append(appInfo.getStartTime()).append("\",\"")
      .append(appInfo.getLaunchTime()).append("\",\"")
      .append(appInfo.getFinishTime()).append("\",\"")
      .append(appInfo.getState()).append("\",\"")
      .append(appInfo.getFinalStatus()).append("\",\"")
      .append(printAppInfo(appInfo.getRunningContainers()))
      .append("\",\"")
      .append(printAppInfo(appInfo.getAllocatedVCores()))
      .append("\",\"")
      .append(printAppInfo(appInfo.getAllocatedMB()))
      .append("\",\"")
      .append(printAppInfo(appInfo.getReservedVCores()))
      .append("\",\"")
      .append(printAppInfo(appInfo.getReservedMB()))
      .append("\",\"")
      // Progress bar
      .append("<br title='").append(percent)
      .append("'> <div class='").append(C_PROGRESSBAR).append("' title='")
      .append(join(percent, '%')).append("'> ").append("<div class='")
      .append(C_PROGRESSBAR_VALUE).append("' style='")
      .append(join("width:", percent, '%')).append("'> </div> </div>")
      .append("\",\"<a href='");

      String trackingURL =
        !appInfo.isTrackingUrlReady()? "#" : appInfo.getTrackingUrlPretty();

      appsTableData.append(trackingURL).append("'>")
      .append(appInfo.getTrackingUI()).append("</a>\"],\n");

    }
    if(appsTableData.charAt(appsTableData.length() - 2) == ',') {
      appsTableData.delete(appsTableData.length()-2, appsTableData.length()-1);
    }
    appsTableData.append("]");
    html.script().$type("text/javascript").
        __("var appsTableData=" + appsTableData).__();

    tbody.__().__();
  }
}
