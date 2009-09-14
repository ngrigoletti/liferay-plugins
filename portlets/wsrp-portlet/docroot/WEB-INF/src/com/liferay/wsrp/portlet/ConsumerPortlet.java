/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.wsrp.portlet;

import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.axis.SimpleHTTPSender;
import com.liferay.util.servlet.PortletResponseUtil;
import com.liferay.wsrp.model.WSRPConsumer;
import com.liferay.wsrp.model.WSRPConsumerPortlet;
import com.liferay.wsrp.service.WSRPConsumerLocalServiceUtil;
import com.liferay.wsrp.service.WSRPConsumerPortletLocalServiceUtil;
import com.liferay.wsrp.util.ExtensionUtil;
import com.liferay.wsrp.util.WSRPConsumerManager;
import com.liferay.wsrp.util.WSRPConsumerManagerFactory;
import com.liferay.wsrp.util.WebKeys;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;

import oasis.names.tc.wsrp.v2.intf.WSRP_v2_Markup_PortType;
import oasis.names.tc.wsrp.v2.types.BlockingInteractionResponse;
import oasis.names.tc.wsrp.v2.types.ClientData;
import oasis.names.tc.wsrp.v2.types.CookieProtocol;
import oasis.names.tc.wsrp.v2.types.GetMarkup;
import oasis.names.tc.wsrp.v2.types.InitCookie;
import oasis.names.tc.wsrp.v2.types.InteractionParams;
import oasis.names.tc.wsrp.v2.types.MarkupContext;
import oasis.names.tc.wsrp.v2.types.MarkupParams;
import oasis.names.tc.wsrp.v2.types.MarkupResponse;
import oasis.names.tc.wsrp.v2.types.MarkupType;
import oasis.names.tc.wsrp.v2.types.NamedString;
import oasis.names.tc.wsrp.v2.types.NavigationalContext;
import oasis.names.tc.wsrp.v2.types.PerformBlockingInteraction;
import oasis.names.tc.wsrp.v2.types.PortletContext;
import oasis.names.tc.wsrp.v2.types.PortletDescription;
import oasis.names.tc.wsrp.v2.types.RuntimeContext;
import oasis.names.tc.wsrp.v2.types.ServiceDescription;
import oasis.names.tc.wsrp.v2.types.SessionContext;
import oasis.names.tc.wsrp.v2.types.SessionParams;
import oasis.names.tc.wsrp.v2.types.StateChange;
import oasis.names.tc.wsrp.v2.types.Templates;
import oasis.names.tc.wsrp.v2.types.UpdateResponse;
import oasis.names.tc.wsrp.v2.types.UploadContext;
import oasis.names.tc.wsrp.v2.types.UserContext;

import org.apache.axis.message.MessageElement;

/**
 * <a href="ConsumerPortlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ConsumerPortlet extends GenericPortlet {

	public static final String PORTLET_NAME_PREFIX = "WSRP_";

	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		try {
			doProcessAction(actionRequest, actionResponse);
		}
		catch (IOException ioe) {
			throw ioe;
		}
		catch (PortletException pe) {
			throw pe;
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		try {
			doRender(renderRequest, renderResponse);
		}
		catch (IOException ioe) {
			throw ioe;
		}
		catch (PortletException pe) {
			throw pe;
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		try {
			doServeResource(resourceRequest, resourceResponse);
		}
		catch (IOException ioe) {
			throw ioe;
		}
		catch (PortletException pe) {
			throw pe;
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	protected void addFormField(
		List<NamedString> formParameters, String name, String[] values) {

		for (String value : values) {
			NamedString formParameter = new NamedString();

			formParameter.setName(name);
			formParameter.setValue(value);

			formParameters.add(formParameter);
		}
	}

	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		WSRPConsumerPortlet wsrpConsumerPortlet = getWSRPConsumerPortlet();

		WSRPConsumer wsrpConsumer =
			WSRPConsumerLocalServiceUtil.getWSRPConsumer(
				wsrpConsumerPortlet.getWsrpConsumerId());

		WSRPConsumerManager wsrpConsumerManager =
			WSRPConsumerManagerFactory.getWSRPConsumerManager(wsrpConsumer);

		InteractionParams interactionParams = new InteractionParams();
		MarkupParams markupParams = new MarkupParams();
		PortletContext portletContext = new PortletContext();
		RuntimeContext runtimeContext = new RuntimeContext();
		UserContext userContext = new UserContext();

		initContexts(
			actionRequest, actionResponse, wsrpConsumerPortlet,
			wsrpConsumerManager, interactionParams, markupParams,
			portletContext, runtimeContext, userContext);

		PerformBlockingInteraction performBlockingInteraction =
			new PerformBlockingInteraction();

		performBlockingInteraction.setInteractionParams(interactionParams);
		performBlockingInteraction.setMarkupParams(markupParams);
		performBlockingInteraction.setPortletContext(portletContext);
		performBlockingInteraction.setRegistrationContext(
			wsrpConsumer.getRegistrationContext());
		performBlockingInteraction.setRuntimeContext(runtimeContext);
		performBlockingInteraction.setUserContext(userContext);

		WSRP_v2_Markup_PortType markupService = getMarkupService(
			actionRequest, wsrpConsumerManager, wsrpConsumer);

		BlockingInteractionResponse blockingInteractionResponse =
			markupService.performBlockingInteraction(
				performBlockingInteraction);

		blockingInteractionResponse.getUpdateResponse();

		processBlockingInteractionResponse(
			actionRequest, actionResponse, blockingInteractionResponse);
	}

	protected void doRender(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		PortletSession portletSession = renderRequest.getPortletSession();

		MarkupContext markupContext =
			(MarkupContext)portletSession.getAttribute(WebKeys.MARKUP_CONTEXT);

		if (markupContext != null) {
			portletSession.removeAttribute(WebKeys.MARKUP_CONTEXT);
		}
		else {
			MarkupResponse markupResponse = getMarkupResponse(
				renderRequest, renderResponse);

			markupContext = markupResponse.getMarkupContext();
		}

		renderResponse.setContentType(ContentTypes.TEXT_HTML_UTF8);

		String content = rewriteURLs(
			renderResponse, markupContext.getItemString());

		PortletResponseUtil.write(renderResponse, content);
	}

	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		String url = resourceRequest.getParameter("wsrp-url");

		proxyURL(resourceRequest, resourceResponse, new URL(url));
	}

	protected MarkupResponse getMarkupResponse(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		PortletSession portletSession = portletRequest.getPortletSession();

		WSRPConsumerPortlet wsrpConsumerPortlet = getWSRPConsumerPortlet();

		WSRPConsumer wsrpConsumer =
			WSRPConsumerLocalServiceUtil.getWSRPConsumer(
				wsrpConsumerPortlet.getWsrpConsumerId());

		WSRPConsumerManager wsrpConsumerManager =
			WSRPConsumerManagerFactory.getWSRPConsumerManager(wsrpConsumer);

		MarkupParams markupParams = new MarkupParams();
		PortletContext portletContext = new PortletContext();
		RuntimeContext runtimeContext = new RuntimeContext();
		UserContext userContext = new UserContext();

		initContexts(
			portletRequest, portletResponse, wsrpConsumerPortlet,
			wsrpConsumerManager, markupParams, portletContext, runtimeContext,
			userContext);

		GetMarkup getMarkup = new GetMarkup();

		getMarkup.setMarkupParams(markupParams);

		PortletContext existingPortletContext =
			(PortletContext)portletSession.getAttribute(
				WebKeys.PORTLET_CONTEXT);

		if (existingPortletContext != null) {
			getMarkup.setPortletContext(existingPortletContext);
		}
		else {
			getMarkup.setPortletContext(portletContext);
		}

		getMarkup.setRegistrationContext(wsrpConsumer.getRegistrationContext());
		getMarkup.setRuntimeContext(runtimeContext);
		getMarkup.setUserContext(userContext);

		WSRP_v2_Markup_PortType markupService = getMarkupService(
			portletRequest, wsrpConsumerManager, wsrpConsumer);

		MarkupResponse markupResponse = markupService.getMarkup(getMarkup);

		processMarkupResponse(portletRequest, portletResponse, markupResponse);

		return markupResponse;
	}

	protected WSRP_v2_Markup_PortType getMarkupService(
			PortletRequest portletRequest,
			WSRPConsumerManager wsrpConsumerManager, WSRPConsumer wsrpConsumer)
		throws Exception {

		PortletSession portletSession = portletRequest.getPortletSession();

		WSRP_v2_Markup_PortType markupService =
			(WSRP_v2_Markup_PortType)portletSession.getAttribute(
				WebKeys.MARKUP_SERVICE, PortletSession.APPLICATION_SCOPE);

		if (markupService == null) {
			markupService = wsrpConsumerManager.getMarkupService();

			ServiceDescription serviceDescription =
				wsrpConsumerManager.getServiceDescription();

			String cookie = (String)portletSession.getAttribute(
				WebKeys.COOKIE);

			CookieProtocol cookieProtocol =
				serviceDescription.getRequiresInitCookie();

			if ((cookie == null) &&
				(cookieProtocol != null)) {

				String cookieProtocolValue = cookieProtocol.getValue();

				if (cookieProtocolValue.equals(CookieProtocol._perGroup) ||
					cookieProtocolValue.equals(CookieProtocol._perUser)) {

					InitCookie initCookie = new InitCookie();

					initCookie.setRegistrationContext(
						wsrpConsumer.getRegistrationContext());
					markupService.initCookie(initCookie);

					cookie = SimpleHTTPSender.getCurrentCookie();

					portletSession.setAttribute(
						WebKeys.COOKIE, cookie,
						PortletSession.APPLICATION_SCOPE);
				}
			}

			portletSession.setAttribute(
				WebKeys.MARKUP_SERVICE, markupService,
				PortletSession.APPLICATION_SCOPE);
		}

		return markupService;
	}

	protected PortletMode getPortletMode(String portletMode) {
		return new PortletMode(portletMode.substring(5));
	}

	protected WindowState getWindowState(String windowState) {
		return new WindowState(windowState.substring(5));
	}

	protected WSRPConsumerPortlet getWSRPConsumerPortlet() throws Exception {
		String portletName = getPortletConfig().getPortletName();

		int pos = portletName.indexOf(
			StringPool.UNDERLINE, PORTLET_NAME_PREFIX.length());

		long wsrpConsumerPortletId = GetterUtil.getLong(
			portletName.substring(pos + 1));

		WSRPConsumerPortlet wsrpConsumerPortlet =
			WSRPConsumerPortletLocalServiceUtil.getWSRPConsumerPortlet(
				wsrpConsumerPortletId);

		return wsrpConsumerPortlet;
	}

	protected void initContexts(
			ActionRequest actionRequest, ActionResponse actionResponse,
			WSRPConsumerPortlet wsrpConsumerPortlet,
			WSRPConsumerManager wsrpConsumerManager,
			InteractionParams interactionParams, MarkupParams markupParams,
			PortletContext portletContext, RuntimeContext runtimeContext,
			UserContext userContext)
		throws Exception {

		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			actionRequest);

		initContexts(
			actionRequest, actionResponse, wsrpConsumerPortlet,
			wsrpConsumerManager, markupParams, portletContext, runtimeContext,
			userContext);

		interactionParams.setPortletStateChange(StateChange.cloneBeforeWrite);

		String interactionState = actionRequest.getParameter(
			"wsrp-interactionState");

		interactionParams.setInteractionState(interactionState);

		String contentType = request.getContentType();

		if (Validator.isNotNull(contentType) &&
			contentType.startsWith(ContentTypes.MULTIPART_FORM_DATA)) {

			processMultipartForm(
				actionRequest, actionResponse, interactionParams);
		}
		else {
			processFormParameters(
				actionRequest, actionResponse, interactionParams);
		}
	}

	protected void initContexts(
			PortletRequest portletRequest, PortletResponse portletResponse,
			WSRPConsumerPortlet wsrpConsumerPortlet,
			WSRPConsumerManager wsrpConsumerManager, MarkupParams markupParams,
			PortletContext portletContext, RuntimeContext runtimeContext,
			UserContext userContext)
		throws Exception {

		PortletSession portletSession = portletRequest.getPortletSession();

		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			portletRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		// Markup params

		List<NamedString> clientAttributes = new ArrayList<NamedString>();

		Enumeration<String> enu = request.getHeaderNames();

		while (enu.hasMoreElements()) {
			String name = enu.nextElement();

			String value = request.getHeader(name);

			NamedString clientAttribute = new NamedString();

			clientAttribute.setName(name);
			clientAttribute.setValue(value);

			clientAttributes.add(clientAttribute);
		}

		User user = themeDisplay.getUser();

		clientAttributes.add(
			new NamedString(
				user.getEmailAddress(), HttpHeaders.LIFERAY_EMAIL_ADDRESS));
		clientAttributes.add(
			new NamedString(
				user.getScreenName(), HttpHeaders.LIFERAY_SCREEN_NAME));
		clientAttributes.add(
			new NamedString(
				String.valueOf(user.getUserId()),
				HttpHeaders.LIFERAY_USER_ID));

		ClientData clientData = new ClientData();

		clientData.setClientAttributes(
			clientAttributes.toArray(new NamedString[clientAttributes.size()]));

		clientData.setRequestVerb(HttpMethods.GET);
		clientData.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));

		markupParams.setClientData(clientData);

		List<Locale> locales = Collections.list(portletRequest.getLocales());

		String[] localesArray = new String[locales.size()];

		for (int i = 0; i < locales.size(); i++) {
			Locale locale = locales.get(i);

			localesArray[i] = locale.toString();
		}

		markupParams.setLocales(localesArray);

		markupParams.setMarkupCharacterSets(_CHAR_SETS);
		markupParams.setMimeTypes(_MIME_TYPES);
		markupParams.setMode("wsrp:" + portletRequest.getPortletMode());
		markupParams.setWindowState("wsrp:" + portletRequest.getWindowState());

		PortletDescription portletDescription =
			wsrpConsumerManager.getPortletDescription(
				wsrpConsumerPortlet.getPortletHandle());

		MarkupType[] markupTypes = portletDescription.getMarkupTypes();

		for (MarkupType markupType : markupTypes) {
			if (markupType.getMimeType().equalsIgnoreCase(
					ContentTypes.TEXT_HTML)) {

				markupParams.setValidNewModes(markupType.getModes());
				markupParams.setValidNewWindowStates(
					markupType.getWindowStates());
			}
		}

		// Navigational context

		NavigationalContext navigationalContext = new NavigationalContext();

		String navigationalState = portletRequest.getParameter(
			"wsrp-navigationalState");

		navigationalContext.setOpaqueValue(navigationalState);

		Map<String, String[]> publicParameterMap =
			portletRequest.getPublicParameterMap();

		Set<String> names = publicParameterMap.keySet();
		List<NamedString> publicValues = new ArrayList<NamedString>();

		for (String name : names) {
			String[] values = publicParameterMap.get(name);

			for (String value : values) {
				NamedString publicValue = new NamedString();

				publicValue.setName(name);
				publicValue.setValue(value);

				publicValues.add(publicValue);
			}
		}

		navigationalContext.setPublicValues(
			publicValues.toArray(new NamedString[publicValues.size()]));

		markupParams.setNavigationalContext(navigationalContext);

		processFormParameters(portletRequest, portletResponse, markupParams);

		// Portlet context

		portletContext.setPortletHandle(wsrpConsumerPortlet.getPortletHandle());

		// Runtime context

		runtimeContext.setNamespacePrefix(portletResponse.getNamespace());
		runtimeContext.setPortletInstanceKey(portletResponse.getNamespace());

		SessionContext sessionContext =
			(SessionContext)portletSession.getAttribute(
				WebKeys.SESSION_CONTEXT);

		if (sessionContext != null) {
			SessionParams sessionParams = new SessionParams();

			sessionParams.setSessionID(sessionContext.getSessionID());

			runtimeContext.setSessionParams(sessionParams);
		}

		runtimeContext.setUserAuthentication("wsrp:password");

		if (portletDescription.getDoesUrlTemplateProcessing()) {
			Templates templates = new Templates();

			templates.setBlockingActionTemplate(_BLOCKING_ACTION_TEMPLATE);
			templates.setRenderTemplate(_RENDER_TEMPLATE);
			templates.setResourceTemplate(_RESOURCE_TEMPLATE);
			templates.setSecureBlockingActionTemplate(
				_BLOCKING_ACTION_TEMPLATE);
			templates.setSecureRenderTemplate(_RENDER_TEMPLATE);
			templates.setSecureResourceTemplate(_RESOURCE_TEMPLATE);

			runtimeContext.setTemplates(templates);
		}

		// User context

		userContext.setUserCategories(new String[] {RoleConstants.USER});
		userContext.setUserContextKey(String.valueOf(themeDisplay.getUserId()));
	}

	protected boolean isReservedParameter(String name) {
		if (name.startsWith("wsrp-")) {
			return true;
		}
		else {
			return false;
		}
	}

	protected void processBlockingInteractionResponse(
			ActionRequest actionRequest, ActionResponse actionResponse,
			BlockingInteractionResponse blockingInteractionResponse)
		throws Exception {

		PortletSession portletSession = actionRequest.getPortletSession();

		String redirectURL = blockingInteractionResponse.getRedirectURL();

		if (Validator.isNotNull(redirectURL)) {
			sendRedirect(actionResponse, redirectURL);

			return;
		}

		UpdateResponse updateResponse =
			blockingInteractionResponse.getUpdateResponse();

		if (updateResponse == null) {
			return;
		}

		portletSession.setAttribute(
			WebKeys.MARKUP_CONTEXT, updateResponse.getMarkupContext());

		NavigationalContext navigationalContext =
			updateResponse.getNavigationalContext();

		if (navigationalContext != null) {
			String opaqueValue = navigationalContext.getOpaqueValue();

			if (opaqueValue != null) {
				actionResponse.setRenderParameter(
					"wsrp-navigationalState", opaqueValue);
			}

			NamedString[] publicValues = navigationalContext.getPublicValues();

			if (publicValues != null) {
				for (NamedString publicValue : publicValues) {
					String name = publicValue.getName();
					String value = publicValue.getValue();

					if (Validator.isNotNull(value)) {
						actionResponse.setRenderParameter(name, value);
					}
					else {
						actionResponse.removePublicRenderParameter(name);
					}
				}
			}
		}

		PortletContext portletContext = updateResponse.getPortletContext();

		if (portletContext != null) {
			portletSession.setAttribute(
				WebKeys.PORTLET_CONTEXT, portletContext);
		}

		SessionContext sessionContext = updateResponse.getSessionContext();

		if (sessionContext != null) {
			portletSession.setAttribute(
				WebKeys.SESSION_CONTEXT, sessionContext);
		}

		String portletMode = updateResponse.getNewMode();

		if (Validator.isNotNull(portletMode)) {
			actionResponse.setPortletMode(getPortletMode(portletMode));
		}

		String windowState = updateResponse.getNewWindowState();

		if (Validator.isNotNull(windowState)) {
			actionResponse.setWindowState(getWindowState(windowState));
		}
	}

	protected void processFormParameters(
		ActionRequest actionRequest, ActionResponse actionResponse,
		InteractionParams interactionParams) {

		List<NamedString> formParameters = new ArrayList<NamedString>();

		Enumeration<String> enu = actionRequest.getParameterNames();

		while (enu.hasMoreElements()) {
			String name = enu.nextElement();

			if (isReservedParameter(name)) {
				continue;
			}

			String[] values = actionRequest.getParameterValues(name);

			if (values == null) {
				continue;
			}

			addFormField(formParameters, name, values);
		}

		if (!formParameters.isEmpty()) {
			interactionParams.setFormParameters(
				formParameters.toArray(new NamedString[formParameters.size()]));
		}
	}

	protected void processFormParameters(
		PortletRequest portletRequest, PortletResponse portletResponse,
		MarkupParams markupParams) {

		List<MessageElement> formParameters = new ArrayList<MessageElement>();

		Enumeration<String> enu = portletRequest.getParameterNames();

		while (enu.hasMoreElements()) {
			String name = enu.nextElement();

			if (isReservedParameter(name)) {
				continue;
			}

			String[] values = portletRequest.getParameterValues(name);

			if (values == null) {
				continue;
			}

			for (String value : values) {
				ExtensionUtil.addMessageElement(formParameters, name, value);
			}
		}

		if (!formParameters.isEmpty()) {
			markupParams.setExtensions(
				ExtensionUtil.getExtensions(formParameters));
		}
	}

	protected void processMarkupResponse(
		PortletRequest portletRequest, PortletResponse portletResponse,
		MarkupResponse markupResponse) {

		PortletSession portletSession = portletRequest.getPortletSession();

		SessionContext sessionContext = markupResponse.getSessionContext();

		if (sessionContext != null) {
			portletSession.setAttribute(
				WebKeys.SESSION_CONTEXT, sessionContext);
		}
	}

	protected void processMultipartForm(
			ActionRequest actionRequest, ActionResponse actionResponse,
			InteractionParams interactionParams)
		throws Exception {

		List<NamedString> formParameters = new ArrayList<NamedString>();
		List<UploadContext> uploadContexts = new ArrayList<UploadContext>();

		UploadPortletRequest uploadPortletRequest =
			PortalUtil.getUploadPortletRequest(actionRequest);

		Enumeration<String> enu = uploadPortletRequest.getParameterNames();

		while (enu.hasMoreElements()) {
			String name = enu.nextElement();

			if (isReservedParameter(name)) {
				continue;
			}

			if (uploadPortletRequest.isFormField(name)) {
				String[] values = actionRequest.getParameterValues(name);

				addFormField(formParameters, name, values);
			}
			else {
				UploadContext uploadContext = new UploadContext();

				String contentType = uploadPortletRequest.getContentType(name);

				uploadContext.setMimeType(contentType);

				StringBuilder sb = new StringBuilder();

				sb.append("form-data; ");
				sb.append("name=");
				sb.append(name);
				sb.append("; filename=");
				sb.append(uploadPortletRequest.getFileName(name));

				NamedString mimeAttribute = new NamedString();

				mimeAttribute.setName(HttpHeaders.CONTENT_DISPOSITION);
				mimeAttribute.setValue(sb.toString());

				uploadContext.setMimeAttributes(
					new NamedString[] {mimeAttribute});

				File file = uploadPortletRequest.getFile(name);

				byte[] bytes = FileUtil.getBytes(file);

				if (bytes == null) {
					continue;
				}

				uploadContext.setUploadData(bytes);

				uploadContexts.add(uploadContext);
			}
		}

		if (!formParameters.isEmpty()) {
			interactionParams.setFormParameters(
				formParameters.toArray(new NamedString[formParameters.size()]));
		}

		if (!uploadContexts.isEmpty()) {
			interactionParams.setUploadContexts(
				uploadContexts.toArray(
					new UploadContext[uploadContexts.size()]));
		}
	}

	protected void proxyURL(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse,
			URL url)
		throws Exception {

		PortletSession portletSession = resourceRequest.getPortletSession();

		String cookie = (String)portletSession.getAttribute(WebKeys.COOKIE);

		URLConnection urlConnection = url.openConnection();

		urlConnection.setRequestProperty("Cookie", cookie);

		urlConnection.connect();

		resourceResponse.setContentLength(urlConnection.getContentLength());
		resourceResponse.setContentType(urlConnection.getContentType());

		PortletResponseUtil.write(
			resourceResponse, urlConnection.getInputStream());
	}

	protected String rewriteURL(
			PortletResponse portletResponse, Map<String, String> parameterMap)
		throws Exception {

		LiferayPortletResponse liferayPortletResponse =
			(LiferayPortletResponse)portletResponse;

		String lifecycle = parameterMap.get("wsrp-urlType");

		LiferayPortletURL liferayPortletURL = null;

		if (lifecycle.equals("blockingAction")) {
			liferayPortletURL =
				(LiferayPortletURL)liferayPortletResponse.createActionURL();
		}
		else if (lifecycle.equals("render")) {
			liferayPortletURL =
				(LiferayPortletURL)liferayPortletResponse.createRenderURL();
		}
		else if (lifecycle.equals("resource")) {
			liferayPortletURL =
				(LiferayPortletURL)liferayPortletResponse.createResourceURL();
		}

		boolean encodeURL = false;

		for (Map.Entry<String, String> parameter : parameterMap.entrySet()) {
			String name = parameter.getKey();
			String value = parameter.getValue();

			if (name.equals("wsrp-extensions") && value.equals("encodeURL")) {
				encodeURL = true;
			}
			else if (name.equals("wsrp-mode")) {
				liferayPortletURL.setPortletMode(getPortletMode(value));
			}
			else if (name.equals("wsrp-resourceID")) {
				liferayPortletURL.setResourceID(value);
			}
			else if (name.equals("wsrp-urlType")) {
			}
			else if (name.equals("wsrp-windowState")) {
				liferayPortletURL.setWindowState(getWindowState(value));
			}
			else if (name.equals("wsrp-navigationalValues")) {
				Matcher navigationalValuesMatcher =
					_navigationalValuesPattern.matcher(value);

				while (navigationalValuesMatcher.find()) {
					String navigationalValuesName =
						navigationalValuesMatcher.group(1);
					String navigationalValuesValue =
						navigationalValuesMatcher.group(2);

					if (Validator.isNull(navigationalValuesValue)) {
						liferayPortletURL.removePublicRenderParameter(
							navigationalValuesName);
					}
					else {
						liferayPortletURL.setParameter(
							navigationalValuesName, navigationalValuesValue,
							true);
					}
				}
			}
			else {
				liferayPortletURL.setParameter(name, value);
			}
		}

		String url = liferayPortletURL.toString();

		if (encodeURL) {
			url = HttpUtil.encodeURL(url);
		}

		return url;
	}

	protected String rewriteURLs(
			PortletResponse portletResponse, String content)
		throws Exception {

		Matcher rewriteMatcher = _rewritePattern.matcher(content);

		StringBuffer sb = new StringBuffer();

		while (rewriteMatcher.find()) {
			String namespace = rewriteMatcher.group(1);
			String url = rewriteMatcher.group(2);
			String extensionURL1 = rewriteMatcher.group(3);
			String extensionURL2 = rewriteMatcher.group(4);

			String replacement = null;

			Map<String, String> parameterMap =
				new HashMap<String, String>();

			if (Validator.isNotNull(namespace)) {
				rewriteMatcher.appendReplacement(
					sb, portletResponse.getNamespace());
			}
			else if (Validator.isNotNull(url)) {
				Matcher parameterMatcher = _parameterPattern.matcher(url);

				while (parameterMatcher.find()) {
					String name = parameterMatcher.group(1);
					String value = parameterMatcher.group(2);

					if (Validator.isNull(value) ||
						value.equals(StringPool.DOUBLE_QUOTE)) {

						continue;
					}

					parameterMap.put(name, HttpUtil.decodeURL(value));
				}

				rewriteMatcher.appendReplacement(
					sb, rewriteURL(portletResponse, parameterMap));
			}
			else if (Validator.isNotNull(extensionURL1)) {
				parameterMap.put("wsrp-urlType", "render");
				parameterMap.put("wsrp-windowState", "wsrp:normal");

				replacement =
					"location.href = '" +
						rewriteURL(portletResponse, parameterMap) + "'";

				rewriteMatcher.appendReplacement(sb, replacement);
			}
			else if (Validator.isNotNull(extensionURL2)) {
				parameterMap.put("wsrp-urlType", "render");
				parameterMap.put("wsrp-windowState", "wsrp:normal");

				replacement =
					"href=\"" + rewriteURL(portletResponse, parameterMap) +
						"\"";

				rewriteMatcher.appendReplacement(sb, replacement);
			}
		}

		rewriteMatcher.appendTail(sb);

		return sb.toString();
	}

	protected void sendRedirect(
			ActionResponse actionResponse, String redirectURL)
		throws Exception {

		redirectURL = rewriteURLs(actionResponse, redirectURL);

		actionResponse.sendRedirect(redirectURL);
	}

	private static final String _BLOCKING_ACTION_TEMPLATE =
		"wsrp_rewrite?wsrp-urlType=blockingAction&" +
		"wsrp-navigationalState={wsrp-navigationalState}&" +
		"wsrp-navigationalValues={wsrp-navigationalValues}&" +
		"wsrp-interactionState={wsrp-interactionState}&" +
		"wsrp-mode={wsrp-mode}&wsrp-windowState={wsrp-windowState}" +
		"&wsrp-fragmentID={wsrp-fragmentID}/wsrp_rewrite";

	private static final String[] _CHAR_SETS = {StringPool.UTF8};

	private static final String[] _MIME_TYPES = {ContentTypes.TEXT_HTML};

	private static final String _RENDER_TEMPLATE =
		"wsrp_rewrite?wsrp-urlType=render&" +
		"wsrp-navigationalState={wsrp-navigationalState}&" +
		"wsrp-navigationalValues={wsrp-navigationalValues}&" +
		"wsrp-mode={wsrp-mode}&wsrp-windowState={wsrp-windowState}&" +
		"wsrp-fragmentID={wsrp-fragmentID}/wsrp_rewrite";

	private static final String _RESOURCE_TEMPLATE =
		"wsrp_rewrite?wsrp-urlType=resource&wsrp-url={wsrp-url}&" +
		"wsrp-resourceID={wsrp-resourceID}&" +
		"wsrp-preferOperation={wsrp-preferOperation}&" +
		"wsrp-resourceState={wsrp-resourceState}&" +
		"wsrp-requiresRewrite={wsrp-requiresRewrite}&" +
		"wsrp-resourceCacheability={wsrp-resourceCacheability}/wsrp_rewrite";

	private static Pattern _navigationalValuesPattern = Pattern.compile(
		"(?:([^&=]+)(?:=([^&=]*))?)&");
	private static Pattern _parameterPattern = Pattern.compile(
		"(?:([^&]+)=([^&]*))(?:&amp;|&)?");
	private static Pattern _rewritePattern = Pattern.compile(
		"(wsrp_rewrite_)|(?:wsrp_rewrite\\?([^\\s/]+)/wsrp_rewrite)|" +
		"(?:location\\.href\\s*=\\s*'(/widget/c/portal/layout(?:[^']+))')|" +
		"(?:href\\s*=\\s*\"(/widget/c/portal/layout(?:[^\"]+))\")");

}