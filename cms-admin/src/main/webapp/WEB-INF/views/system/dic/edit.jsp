<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>

<div class="modal fade" id="dic_edit_modal_div" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-titlCommonControllere">
                    <c:choose>
                        <c:when test="${empty dic}">
                            增加字典
                        </c:when>
                        <c:otherwise>
                            修改字典
                        </c:otherwise>
                    </c:choose>
                </h4>
            </div>
            <div class="modal-body">
                <form id="dic_edit_form">
                    <div class="content form-horizontal">
                        <div class="form-group">
                            <div class="col-md-12">
                                <tags:message content="${message}"/>
                                <div class="form-group">
                                    <label class="control-label col-md-3">字典名称(<span class="required">*</span>):</label>

                                    <div class="col-md-7">
                                        <div class="input_tip">
                                            <input type="hidden" name="id" value="${dic.id}"/>
                                            <input type="text" class="form-control validate[required]" name="name"
                                                   id="name"
                                                   value="${dic.name}" placeholder="字典名称"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3">字典代码(<span class="required">*</span>):</label>

                                    <div class="col-md-7">
                                        <div class="input_tip">
                                            <input type="text" class="form-control validate[required,custom[onlyLetterNumberUnderline]]" name="code"
                                                   id="code"
                                                   value="${dic.code}" placeholder="字典代码"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3">描述:</label>

                                    <div class="col-md-7">
                                        <div class="input_tip">
                                            <textarea class="form-control" name="description" id="description"
                                                      placeholder="描述信息">${dic.description}</textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline green"
                        onclick="$.DicController.edit();">
                    <i class="fa fa-save"></i>保存
                </button>
                <button class="btn grey-salsa btn-outline" data-dismiss="modal" aria-hidden="true">
                    <i class="fa fa-close"></i>关闭
                </button>
            </div>
        </div>
    </div>
</div>