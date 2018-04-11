<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld"%>
<%@ attribute name="name" type="java.lang.String" required="false"
	description="字段名称"%>
<%@ attribute name="desc" type="java.lang.String" required="false"
	description="字段描述"%>
<%@ attribute name="value" type="java.lang.String" required="false"
	description="值"%>
<%@ attribute name="readOnly" type="java.lang.Boolean" required="false"
	description="是否只读"%>

<div class="form-group">
	<label class="control-label col-md-3">${desc}: </label>

	<div class="col-md-9">
		<input type="hidden" id="${name}" name="${name}" value="${value}">

		<c:if test="${empty value}">
			<c:set var="imageUrl" value="${ctx}/static/img/no-image.png" />
			<c:set var="fileinputMethod" value="fileinput-new" />
		</c:if>
		<c:if test="${! empty value}">
			<c:set var="imageUrl" value="${fns:getImagePath(value)}" />
			<c:set var="fileinputMethod" value="fileinput-exists" />
		</c:if>
		<div class="fileinput ${fileinputMethod} fileinput_${name}"
			data-provides="fileinput">
			<div class="fileinput-preview thumbnail" data-trigger="fileinput"
				style="width: 200px; height: 200px;">

				<img src="${imageUrl}" alt="" />
			</div>
			<c:if test="${!readOnly}">
				<div>
					<span class="btn red btn-outline btn-file"> <span
						class="fileinput-new"> 选择图片 </span> <span class="fileinput-exists">
							更换图片 </span> <input type="file" name="..." accept=".jpg,.png">
					</span> <a href="javascript:;" class="btn red fileinput-exists"
						data-dismiss="fileinput"> 移除图片 </a>
				</div>
			</c:if>
		</div>
		<div class="clearfix margin-top-10">
			<span class="label label-info">大小0X0</span>
		</div>
	</div>
</div>