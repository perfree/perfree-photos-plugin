let util, layer,table;
layui.use(['util', 'layer','table'], function () {
    util = layui.util;
    layer = layui.layer;
    table = layui.table;

    queryTable();

    // 查询
    $("#queryBtn").click(function () {
        queryTable();
    });

    // 添加
    $("#addBtn").click(function () {
        layer.open({
            title: "新增图片",
            type: 2,
            offset: '20%',
            area: common.layerArea($("html")[0].clientWidth, 400, 450),
            shadeClose: true,
            anim: 1,
            resize: true,
            maxmin: true,
            content: '/admin/plugin/photo/addPage/'+$("#photosId").val()
        });
    });

});

/**
 * 查询表格数据
 */
function queryTable() {
    table.render({
        elem: '#tableBox',
        url: '/admin/plugin/photo/list',
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        contentType: 'application/json',
        title: '图片列表',
        totalRow: false,
        limit: 10,
        where: {
            form: {
                name: $("#name").val(),
                photosId: $("#photosId").val()
            }
        },
        cols: [[
            {field: 'id', title: 'ID', width: 80, sort: true},
            {field: 'name',   minWidth: 160,title: '图片名称'},
            {field: 'desc',   minWidth: 260,title: '图片描述'},
            {field: 'url',   minWidth: 160,title: '图片', templet: function (d) {
                    return "<img src='/static-plugin/photos"+ d.url +"'>";
                }
            },
            {
                field: 'createTime',
                title: '创建时间',
                sort: true,
                minWidth: 160,
                templet: "<span>{{d.createTime ==null?'':layui.util.toDateString(d.createTime, 'yyyy-MM-dd HH:mm:ss')}}</span>"
            },
            {
                field: 'id', title: '操作', width: 120,
                templet: "<div>" +
                    "<a class='layui-btn layui-btn-normal layui-btn-xs' onclick='editData(\"{{d.id}}\")'>编辑</a> " +
                    "<a class='layui-btn layui-btn-danger layui-btn-xs' onclick='deleteData(\"{{d.id}}\")'>删除</a>" +
                    "</div>"
            }
        ]],
        page: true,
        response: {statusCode: 200},
        parseData: function (res) {
            return {
                "code": res.code,
                "msg": res.msg,
                "count": res.total,
                "data": res.data
            };
        },
        request: {
            pageName: 'pageIndex',
            limitName: 'pageSize'
        }
    });
}

/**
 *
 * @param ids
 */
function deleteData(id) {
    layer.confirm('确定要删除吗?', {icon: 3, title: '提示'}, function (index) {
        $.ajax({
            type: "POST",
            url: "/admin/plugin/photo/del",
            contentType: "application/json",
            data: id,
            success: function (data) {
                if (data.code === 200) {
                    queryTable();
                    layer.msg(data.msg, {icon: 1});
                } else {
                    layer.msg(data.msg, {icon: 2});
                }
            },
            error: function (data) {
                layer.msg("删除失败", {icon: 2});
            }
        });
        layer.close(index);
    });
}

/**
 * 编辑
 * @param id
 */
function editData(id) {
    layer.open({
        title: "编辑相册",
        type: 2,
        offset: '20%',
        area: common.layerArea($("html")[0].clientWidth, 400, 450),
        shadeClose: true,
        anim: 1,
        resize: true,
        maxmin: true,
        content: '/admin/plugin/photo/editPage/' + id
    });
}


/**
 * 进入相册
 * @param data
 */
function toPhotos(data) {
    parent.openTab('', '相册-'+data.name, '/admin/plugin/photo/listPage/' + id, "-10254");
}