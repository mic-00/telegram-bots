<table bordered striped compact>
<tr>
<#list columns as column>
    <th>${column}</th>
</#list>
</tr>
<#list data as row>
    <tr>
    <#list row as value>
        <td>${value}</td>
    </#list>
    </tr>
</#list>
</table>