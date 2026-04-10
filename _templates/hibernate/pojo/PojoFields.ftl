<#--
~ Copyright 2010 - 2025 Red Hat, Inc.
~
~ Licensed under the Apache License, Version 2.0 (the "License");
~ you may not use this file except in compliance with the License.
~ You may obtain a copy of the License at
~
~     http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing, software
~ distributed under the License is distributed on an "AS IS" basis,
~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
~ See the License for the specific language governing permissions and
~ limitations under the License.
-->
<#-- // Fields -->

<#list pojo.getAllPropertiesIterator() as property><#if pojo.getMetaAttribAsBool(property, "gen-property", true)>
    <#if pojo.hasMetaAttribute(property, "field-description")>    /**
        ${pojo.getFieldJavaDoc(property, 0)}
        */
    </#if>
    <#if pojo.hasMetaAttribute(property, "field-annotation")><#list property.metaAttributes["field-annotation"].values as ann>${ann}</#list></#if>
    <#include "GetPropertyAnnotation.ftl"/>
    ${pojo.getFieldModifiers(property)} ${pojo.getJavaTypeName(property, jdk5)} ${c2j.keyWordCheck(property.name)}<#if pojo.hasFieldInitializor(property, jdk5)> = ${pojo.getFieldInitialization(property, jdk5)}</#if>;
</#if>
</#list>