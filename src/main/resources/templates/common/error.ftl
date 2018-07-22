<#assign ctx=request.contextPath/>
<html>
<#include "../common/header.ftl">
<body>
<div id="wrapper" class="toggled">
<#-- 侧边栏sidebar -->
    <#include "../common/nav.ftl">
<#-- 主要内容Content -->
    <div id="page-content-wrapper">
        <div class="container">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <div class="alert alert-dismissable alert-danger">
                        <button type="button" class="close"
                                data-dismiss="alert" aria-hidden="true">×
                        </button>
                        <h3>操作失败!</h3>
                        <h4>${msg!""}</h4>
                        <a href="${ctx}/${returnUrl}">3s后自动跳转</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script>
    setTimeout(function () {
        location.href = '${ctx}/${returnUrl}';
        // history.go(-1);
    }, 3000);
</script>
</body>