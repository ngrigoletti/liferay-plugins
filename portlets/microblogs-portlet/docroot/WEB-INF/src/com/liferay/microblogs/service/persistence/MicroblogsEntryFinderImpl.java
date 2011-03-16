/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.microblogs.service.persistence;

import com.liferay.microblogs.model.MicroblogsEntry;
import com.liferay.microblogs.model.MicroblogsEntryConstants;
import com.liferay.microblogs.model.impl.MicroblogsEntryImpl;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.PortalClassInvoker;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.Iterator;
import java.util.List;

/**
 * @author Jonathan Lee
 */
public class MicroblogsEntryFinderImpl
	extends BasePersistenceImpl implements MicroblogsEntryFinder {

	public MicroblogsEntryFinderImpl() {
		try {
			_joinBySocialRelationSQL = (String)PortalClassInvoker.invoke(
				true, _getMethodKey,
				"com.liferay.portal.service.persistence." +
					"UserFinder.joinBySocialRelation");
		}
		catch (Exception e) {
			_log.error(e,e);
		}
	}

	public static String countByU_VU =
		MicroblogsEntryFinder.class.getName() + ".countByU_VU";

	public static String findByU_VU =
		MicroblogsEntryFinder.class.getName() + ".findByU_VU";

	public int countByU_VU(long userId, long viewUserId)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			StringBundler sb = new StringBundler(3);

			sb.append(CustomSQLUtil.get(countByU_VU));

			sb.append(_GROUP_BY);
			sb.append(_MICROBLOGS_ENTRY_ID);

			String sql = sb.toString();

			sql = StringUtil.replace(
				sql, "[$JOIN_BY_SOCIAL_RELATION$]", _joinBySocialRelationSQL);

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(MicroblogsEntryConstants.TYPE_EVERYONE);
			qPos.add(viewUserId);
			qPos.add(userId);
			qPos.add(viewUserId);

			Iterator<Long> itr = q.list().iterator();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<MicroblogsEntry> findByU_VU(
			long userId, long viewUserId, int start, int end)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			StringBundler sb = new StringBundler(5);

			sb.append(CustomSQLUtil.get(findByU_VU));

			sb.append(_GROUP_BY);
			sb.append(_MICROBLOGS_ENTRY_ID);
			sb.append(_ORDER_BY);
			sb.append(_CREATE_DATE_DESC);

			String sql = sb.toString();

			sql = StringUtil.replace(
				sql, "[$JOIN_BY_SOCIAL_RELATION$]", _joinBySocialRelationSQL);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity("microblogsEntry", MicroblogsEntryImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(MicroblogsEntryConstants.TYPE_EVERYONE);
			qPos.add(viewUserId);
			qPos.add(userId);
			qPos.add(viewUserId);

			return (List<MicroblogsEntry>)QueryUtil.list(
				q, getDialect(), start, end);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	private String _joinBySocialRelationSQL;

	private static final String _AND_COMPANY_ID =
		" AND microblogsEntry.companyId = ";

	private static final String _AND_RECEIVER_ENTRY_ID =
		" AND microblogsEntry.receiverEntryId = ";

	private static final String _AND_RECEIVER_USER_ID =
		" AND microblogsEntry.receiverUserId = ";

	private static final String _AND_SOCIALRELATION_TYPE =
		" AND microblogsEntry.socialRelationType = ";

	private static final String _AND_TAG_IDS = " AND AssetTag.name IN ";

	private static final String _AND_TYPE =
		" AND microblogsEntry.type_ = ";

	private static final String _AND_USER_IDS =
		" AND microblogsEntry.userId IN ";

	private static final String _CREATE_DATE_DESC =
		" microblogsEntry.createDate DESC ";

	private static final String _GROUP_BY = " GROUP BY ";

	private static final String _MICROBLOGS_ENTRY_ID =
		" microblogsEntry.microblogsEntryId ";

	private static final String _ORDER_BY = " ORDER BY ";

	private static MethodKey _getMethodKey = new MethodKey(
		"com.liferay.util.dao.orm.CustomSQL", "get", String.class);

	private static Log _log = LogFactoryUtil.getLog(
		MicroblogsEntryFinderImpl.class);

}