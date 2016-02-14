<#--
    file_gallery.ftl: Display a list of files from the media display portlet
    in a gallery.
    
    Created:    2016-02-14 16:19 by Christian Berndt
    Modified:   2016-02-14 22:33 by Christian Berndt
    Version:    1.0.1
-->

<#assign dLFileEntryTypeService = serviceLocator.findService("com.liferay.portlet.documentlibrary.service.DLFileEntryTypeService") />
<#assign dLFileEntryMetadataLocalService = serviceLocator.findService("com.liferay.portlet.documentlibrary.service.DLFileEntryMetadataLocalService") />
<#assign storageEngine = serviceLocator.findService("com.liferay.portlet.dynamicdatamapping.storage.StorageEngine") />

<div class="container">
    <div class="template gallery file-entries span8 offset2">
        <h1><@liferay.language key="files" /></h1>
        
        <#if entries?has_content>
        
            <#assign i = 1 />
            
            <div class="row-fluid">        
            <#list entries as fileEntry>
            
                <#assign latestFileVersion = fileEntry.getFileVersion() />
                <#assign latestFileVersionStatus = latestFileVersion.getStatus() />
                <#assign fileTitle = httpUtil.encodeURL(htmlUtil.unescape(latestFileVersion.getTitle())) />
                <#assign fileSize = latestFileVersion.getSize() />
                <#assign mimeType = latestFileVersion.getMimeType() />
                
                <#assign imgSrc = "/documents/" + groupId + "/" + fileEntry.getFolderId() + "/" + fileTitle /> 
                <#assign description = latestFileVersion.getDescription() />
                <#assign title =  "" />
                <#assign copyright = "" />
                <#assign style = "background-image: url('" + imgSrc + "?imageThumbnail=2" + "');" />
                
                <#assign fileEntryTypeId = 0 />

                <#assign dlFileVersion = latestFileVersion.getModel() />
                <#assign fileEntryTypeId = dlFileVersion.getFileEntryTypeId() />

                <#if fileEntryTypeId gt 0 >
                
                    <#assign fileEntryType = dLFileEntryTypeService.getFileEntryType(fileEntryTypeId) />
                    <#assign ddmStructures = fileEntryType.getDDMStructures() />               
       
                    <#list ddmStructures as ddmStructure>
                    
                        <#assign fileEntryMetadata = dLFileEntryMetadataLocalService.getFileEntryMetadata(ddmStructure.getStructureId(), latestFileVersion.getFileVersionId()) />
                        <#assign fields = storageEngine.getFields(fileEntryMetadata.getDDMStorageId()) />
                        
                        <#if fields.get("title")??>                     
                            <#assign title = fields.get("title").getValue(locale) /> 
                        </#if>   
                        <#if fields.get("description")??>                     
                            <#assign description = fields.get("description").getValue(locale) /> 
                        </#if>                         
                         <#if fields.get("copyright")??>                     
                            <#assign copyright = fields.get("copyright").getValue(locale) /> 
                        </#if>                         
                              
                    </#list>
                
                </#if>      
       
                <div class="span6">
                    <div class="file-entry" >
                        <a href="${imgSrc}" target="_blank" title="<@liferay.language key="download-file" />">
                            <div class="preview" style="${style}">&nbsp;</div>
                        </a>
                        
                        <#if title?has_content>
                            <div class="title">${title}</div>
                        </#if>
                        
                        <#if description?has_content>
                            <div class="description">${description}</div>
                        </#if>
                        
                        <#if copyright?has_content>
                            <div class="copyright">&copy; ${copyright}</div>
                        </#if>
                        
                        <div class="mime-type">
                            <a href="${imgSrc}" target="_blank" title="<@liferay.language key="download-file" />">
                            ${mimeType} ${(fileSize / (1024 * 1024))?string["0.#"]} MB <span class="icon-arrow-down">&nbsp;</span>
                            </a>
                        </div>
                    </div>
                </div>
            
                <#if i%2 == 0 && i gt 1 >
                    </div>
                    <div class="row-fluid">
                </#if>
                
                <#assign i = i+1 />
                           
            </#list>
            
        <#else>
            <div class="alert alert-info"><@liferay.language key="there-are-no-results" /></div>        
        </#if>        
    </div> <#-- / .file-entries -->
</div> <#-- / .container -->