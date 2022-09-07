var form,table, toast,util,layer;
let formType = 'add';
layui.use(['util', 'toast','table','form','layer'], function () {
    util = layui.util;
    toast = layui.toast;
    form = layui.form;
    table = layui.table;
    layer = layui.layer;
    form.verify({});
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
});

function toAddFunc() {
    $(".addForm")[0].reset();
    form.render();
    formType = 'add';
    $("#restBtn").text("重置");
    $("#form-title").text("添加相册");
}

function addSubmit(data) {
    $.ajax({
        type: "POST",
        url: "/admin/plugin/photos/addPhotos",
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

function updateSubmit(data) {
    $.ajax({
        type: "POST",
        url: "/admin/plugin/photos/editPhotos",
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
                name: $("#queryName").val()
            }
        },
        cols: [[
            {field: 'id', title: 'ID', width: 80, sort: true},
            {field: 'name',   minWidth: 140,title: '名称'},
            {field: 'desc',   minWidth: 200,title: '描述'},
            {field: 'coverUrl',   minWidth: 100,title: '封面', templet: function (d) {
                    return "<img src='"+ d.coverUrl +"' style='height: 30px'>";
                }
            },
            {field: 'isEncryption',   minWidth: 80,title: '加密',templet: function (d) {
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
                    "<a class='pear-btn pear-btn-xs pear-btn-primary' onclick='toPhotos(\"{{d.id}}\", \"{{d.name}}\")'>进入</a> " +
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
            url: "/admin/plugin/photos/del",
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
    $.get("/admin/plugin/photos/getById?id="+id,function(data){
        if (data.code === 200) {
            $("input[name='id']").val(data.data.id);
            $("input[name='name']").val(data.data.name);
            $("textarea[name='desc']").val(data.data.desc);
            $("input[name='coverUrl']").val(data.data.coverUrl);
            $("#form-title").text("修改相册");
            $("#restBtn").text("返回添加");
            formType = 'edit';
        } else {
            parent.toast.error({message: data.msg,position: 'topCenter'});
        }
    });
}


/**
 * 进入相册
 */
function toPhotos(id, name) {
    parent.layui.admin.jump('photos-'+id, '相册-'+name, '/admin/plugin/photo/listPage/' + id);
}