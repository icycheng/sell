<#assign ctx=request.contextPath/>
<html>
<#include "../common/header.ftl">

<body>
<div id="wrapper" class="toggled">

<#--边栏sidebar-->
<#include "../common/nav.ftl">

<#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-5 col-md-offset-2 column">
                    <form role="form" method="post" action="/sell/seller/category/save">
                        <div class="form-group">
                            <label>名字</label>
                            <input name="categoryName" type="text" class="form-control"
                                   value="${(category.categoryName)!''}"/>
                            <span id="nameHelpBlock" class="help-block"></span>
                        </div>
                        <div class="form-group">
                            <label>type</label>
                            <input name="categoryType" type="text" class="form-control"
                                   value="${(category.categoryType)!''}"/>
                            <span id="typeHelpBlock" class="help-block"></span>
                        </div>
                        <input hidden type="text" name="categoryId"
                               value="${(category.categoryId)!''}">
                        <button type="submit" class="btn btn-default btn-success">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
<script src="https://cdn.bootcss.com/jquery/2.1.0/jquery.js"></script>
<script>
    $(function () {
        //表单校验
        $('input[name="categoryName"]').change(function () {
            var parent = $(this).parent();
            var inputName = $(this).val();
            if (inputName === '' || inputName.trim() === '') {
                $('#nameHelpBlock').text('类型名称不能为空');
                parent.addClass('has-error');
            } else {
                $('#nameHelpBlock').text('');
                parent.removeClass('has-error').addClass('has-success');
            }
        });
        $('input[name="categoryType"]').change(function () {
            var parent = $(this).parent();
            var inputType = $(this).val();
            if (inputType === '' || inputType.trim() === '') {
                $('#typeHelpBlock').text('类型编号不能为空');
                parent.addClass('has-error');
            } else if (typeof inputType !== "number") {
                $('#typeHelpBlock').text('类型编号必须为数字');
                parent.addClass('has-error');
            } else {
                $('#typeHelpBlock').text('');
                parent.removeClass('has-error').addClass('has-success');
            }
        });
    });
</script>
</body>
</html>