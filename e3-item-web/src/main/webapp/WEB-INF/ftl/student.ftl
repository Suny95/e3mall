<html>
<head>
    <title>学生</title>
</head>
<body>
    学习信息:
        id: ${student.id}&emsp;&emsp;&emsp;&emsp;姓名:${student.name}&emsp;&emsp;&emsp;&emsp;年龄:${student.age}&emsp;&emsp;&emsp;&emsp;地址:${student.address}<br/>
    学生列表:
        <hr/>
        <table border="1">
            <tr>
                <th>id</th>
                <th>姓名</th>
                <th>年龄</th>
                <th>地址</th>
            </tr>
            <#list stuList as stu>
            <#if stu_index % 2 == 0>
                <tr bgcolor="red">
            <#else >
                <tr bgcolor="green">
            </#if>
                    <td>${stu.id}</td>
                    <td>${stu.name}</td>
                    <td>${stu.age}</td>
                    <td>${stu.address}</td>
                </tr>
            </#list>
        </table>
    当前时间:${date?string('yyyy-MM-dd HH:mm:ss')}<br/>
    null值: ${val!"我是默认值"}<br/>
    判断是否为null<br/>
    <#if val??>
        我有值: ${val}
        <#else>
        我是null
    </#if><br/>
测试引入外部文件:<#include "hello.ftl">

</body>
</html>