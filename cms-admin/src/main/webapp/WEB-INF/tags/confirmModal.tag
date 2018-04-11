<%@ tag language="java" pageEncoding="UTF-8" %>

<script id="confirm_modal_template" type="text/x-dot-template">
    <div id="confirm_modal_dialog" class="modal fade {{ if(it && it.modalCss) { }} {{=it.modalCss}} {{ } }}"
         tabindex="-1" role="basic" aria-hidden="true">
        <div class="modal-dialog {{ if(it && it.dialogCss) { }} {{=it.dialogCss}} {{ } }}">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 class="modal-title">
                        {{
                        if(it && it.title) {
                        }}
                        {{=it.title}}
                        {{
                        } else {
                        }}
                        提示
                        {{
                        }
                        }}
                    </h4>
                </div>
                <div class="modal-body">
                    {{
                    if(it && it.body){
                    }}
                    {{=it.body}}
                    {{
                    }
                    }}
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline green"
                            onclick="{{ if(it && it.event) { }} {{=it.event}} {{ } }}">
                        <i class="fa fa-check"></i>确定
                    </button>
                    <button type="button" class="btn grey-salsa btn-outline" data-dismiss="modal"
							onclick="{{ if(it && it.close) { }} {{=it.close}} {{ } }}">
                        <i class="fa fa-close"></i>关闭
                    </button>
                </div>
            </div>
        </div>
    </div>
</script>