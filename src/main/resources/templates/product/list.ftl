<#assign ctx=request.contextPath/>
<html>
<#include "../common/header.ftl">
<body>
<div id="wrapper" class="toggled">
<#-- 侧边栏sidebar -->
    <#include "../common/nav.ftl">
<#-- 主要内容Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>商品id</th>
                                <th>名称</th>
                                <th>图片</th>
                                <th>单价</th>
                                <th>库存</th>
                                <th>描述</th>
                                <th>类目</th>
                                <th>创建时间</th>
                                <th>修改时间</th>
                                <th colspan="2">操作</th>
                            </tr>
                        </thead>

                        <tbody>
                            <#list ProductInfoPage.content as productInfo>
                            <tr>
                                <td>${productInfo.productId}</td>
                                <td>${productInfo.productName}</td>
                                <td><img src="${productInfo.productIcon}" width="80" height="70"></td>
                                <td>${productInfo.productPrice}</td>
                                <td>${productInfo.productStock}</td>
                                <td>${productInfo.productDescription}</td>
                                <td>${productInfo.categoryType}</td>
                                <td>${productInfo.createTime}</td>
                                <td>${productInfo.updateTime}</td>
                                <td>
                                    <a href="/sell/seller/product/index?productId=${productInfo.productId}">修改</a>
                                <td>
                                <#if productInfo.getProductStatusEnum().desc == "在架">
                                <td>
                                    <a href="/sell/seller/product/off_sale?productId=${productInfo.productId}">下架</a>
                                <td>
                                <#else>
                                <td>
                                    <a href="/sell/seller/product/on_sale?productId=${productInfo.productId}">上架</a>
                                <td>
                                </#if>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>

                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                        <#if currentPage lte 1>
                            <li class="disabled">
                                <a href="javascript:void(0)">上一页</a>
                            </li>
                        <#else>
                            <li>
                                <a href="${ctx}/seller/product/list?page=${currentPage - 1}&size=${size}">上一页</a>
                            </li>
                        </#if>

                        <#list 1..ProductInfoPage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="javascript:void(0)">${index}</a></li>
                            <#else>
                                <li>
                                    <a href="${ctx}/seller/product/list?page=${index}&size=${size}">${index}</a>
                                </li>
                            </#if>
                        </#list>

                        <#if currentPage gte ProductInfoPage.getTotalPages()>
                            <li class="disabled">
                                <a href="javascript:void(0)">下一页</a>
                            </li>
                        <#else>
                            <li>
                                <a href="${ctx}/seller/product/list?page=${currentPage + 1}&size=${size}">下一页</a>
                            </li>
                        </#if>
                        <span style="line-height: 34px; font-size: 16px; margin-left: 15px;">
                            当前第<strong>${currentPage}</strong>页
                            共<strong>${ProductInfoPage.getTotalElements()}</strong>条数据&nbsp;&nbsp;
                        </span>
                    </ul>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>