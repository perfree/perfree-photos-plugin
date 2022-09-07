var form,table, toast,util,layer;
let formType = 'add';
layui.use(['util', 'toast','table','form','layer'], function () {
    util = layui.util;
    toast = layui.toast;
    form = layui.form;
    table = layui.table;
    layer = layui.layer;
    form.on('submit(addForm)', function (data) {
        if (formType === 'add') {
            addSubmit(data);
        } else {
            updateSubmit(data);
        }
        return false;
    });

    $("#restBtn").on("click",function () {
        if (formType === 'add') {
            $(".addForm")[0].reset();
            form.render();
        } else {
            toAddFunc();
        }
    });

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

function toAddFunc() {
    $(".addForm")[0].reset();
    form.render();
    formType = 'add';
    $("#restBtn").text("重置");
    $("#form-title").text("添加图片");
}

function updateSubmit(data) {
    $.ajax({
        type: "POST",
        url: "/admin/plugin/photo/editPhoto",
        contentType: "application/json",
        data: JSON.stringify(data.field),
        success: function (res) {
            if (res.code === 200) {
                queryTable();
                parent.toast.success({message: '更新成功',position: 'topCenter'});
                toAddFunc();
            } else {
                parent.toast.error({message: res.msg,position: 'topCenter'});
            }
        },
        error: function (res) {
            parent.toast.error({message: "更新失败",position: 'topCenter'});
        }
    });
}

function addSubmit(data) {
    data.field.photosId = $("#photosId").val();
    $.ajax({
        type: "POST",
        url: "/admin/plugin/photo/addPhoto",
        contentType: "application/json",
        data: JSON.stringify(data.field),
        success: function (res) {
            if (res.code === 200) {
                queryTable();
                parent.toast.success({message: '添加成功',position: 'topCenter'});
                $(".addForm")[0].reset();
                form.render();
            } else {
                parent.toast.error({message: res.msg,position: 'topCenter'});
            }
        },
        error: function (res) {
            parent.toast.error({message: "添加失败",position: 'topCenter'});
        }
    });
}

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
                name: $("#queryName").val(),
                photosId: $("#photosId").val()
            }
        },
        cols: [[
            {field: 'id', title: 'ID', width: 80, sort: true},
            {field: 'name',   minWidth: 160,title: '图片名称'},
            {field: 'desc',   minWidth: 260,title: '图片描述'},
            {field: 'url',   minWidth: 160,title: '图片', templet: function (d) {
                    return "<img src='"+ d.url +"' style='height: 30px'>";
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
                    "<a class='pear-btn pear-btn-xs pear-btn-primary' onclick='editData(\"{{d.id}}\")'>编辑</a> " +
                    "<a class='pear-btn pear-btn-xs pear-btn-danger' onclick='deleteData(\"{{d.id}}\")'>删除</a>" +
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
                    parent.toast.success({message: "删除成功",position: 'topCenter'});
                } else {
                    parent.toast.error({message: data.msg,position: 'topCenter'});
                }
            },
            error: function (data) {
                parent.toast.error({message: "删除失败",position: 'topCenter'});
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
    $.get("/admin/plugin/photo/getById?id="+id,function(data){
        if (data.code === 200) {
            $("input[name='id']").val(data.data.id);
            $("input[name='name']").val(data.data.name);
            $("textarea[name='desc']").val(data.data.desc);
            $("input[name='url']").val(data.data.url);
            $("#form-title").text("修改图片");
            $("#restBtn").text("返回添加");
            formType = 'edit';
        } else {
            parent.toast.error({message: data.msg,position: 'topCenter'});
        }
    });
}


/**
 * 进入相册
 * @param data
 */
function toPhotos(data) {
    parent.openTab('', '相册-'+data.name, '/admin/plugin/photo/listPage/' + id, "-10254");
}