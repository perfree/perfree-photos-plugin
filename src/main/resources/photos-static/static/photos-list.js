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
            title: "添加相册",
            type: 2,
            offset: '20%',
            area: common.layerArea($("html")[0].clientWidth, 400, 550),
            shadeClose: true,
            anim: 1,
            resize: true,
            maxmin: true,
            content: '/admin/plugin/photos/addPage'
        });
    });

});

/**
 * 查询表格数据
 */
function queryTable() {
    table.render({
        elem: '#tableBox',
        url: '/admin/plugin/photos/list',
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        contentType: 'application/json',
        title: '相册列表',
        totalRow: false,
        limit: 10,
        where: {
            form: {
                name: $("#name").val()
            }
        },
        cols: [[
            {field: 'id', title: 'ID', width: 80, sort: true},
            {field: 'name',   minWidth: 160,title: '相册名称'},
            {field: 'desc',   minWidth: 260,title: '相册描述'},
            {field: 'coverUrl',   minWidth: 160,title: '相册封面', templet: function (d) {
                    return "<img src='/static-plugin/photos"+ d.coverUrl +"'>";
                }
            },
            {field: 'isEncryption',   minWidth: 160,title: '是否加密',templet: function (d) {
                    let str = "否"
                    if (d.isEncryption === 1) {
                        str = "是"
                    }
                    return str;
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
                field: 'id', title: '操作', width: 200,
                templet: "<div>" +
                    "<a class='layui-btn layui-btn-normal layui-btn-xs' onclick='downloadPhotos(\"{{d.id}}\")'>下载</a> " +
                    "<a class='layui-btn layui-btn-normal layui-btn-xs' onclick='toPhotos(\"{{d.id}}\", \"{{d.name}}\")'>进入</a> " +
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
            url: "/admin/plugin/photos/del",
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
        area: common.layerArea($("html")[0].clientWidth, 400, 550),
        shadeClose: true,
        anim: 1,
        resize: true,
        maxmin: true,
        content: '/admin/plugin/photos/editPage/' + id
    });
}


/**
 * 进入相册
 */
function toPhotos(id, name) {
    parent.openTab('', '相册-'+name, '/admin/plugin/photo/listPage/' + id, "-10254");
}

/**
 * 下载相册
 */
function downloadPhotos(id) {
    let form = $("<form>");
    form.attr("style", "display:none");
    form.attr("target", "");
    form.attr("method", "get");//提交方式为post
    form.attr("action", "/admin/plugin/photos/download/" + id);//定义action

    $("body").append(form);
    form.submit();
}