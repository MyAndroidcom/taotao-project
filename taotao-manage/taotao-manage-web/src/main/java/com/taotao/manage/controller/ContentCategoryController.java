package com.taotao.manage.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@Controller
@RequestMapping("content/category")
public class ContentCategoryController {
    
    @Autowired 
    private ContentCategoryService contentCategoryService;
    
    /*
     * 根据父类节点查询
     * */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryListByParentId(
            @RequestParam(value="id",defaultValue="0") Long parentId){
        try{
            ContentCategory record = new ContentCategory();
            record.setParentId(parentId);
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
            if(null==list||list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    /*
     * 新增子节点
     * */
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory){
        try{
            contentCategory.setId(null);
            contentCategory.setIsParent(false);
            contentCategory.setSortOrder(1);
            contentCategory.setStatus(1);
            this.contentCategoryService.save(contentCategory);
            //判断父亲节点的isParent是否为true,不是，需要修改为true
            ContentCategory parent = this.contentCategoryService.queryById(contentCategory.getParentId());
            if (!parent.getIsParent()) {
                parent.setIsParent(true);
                this.contentCategoryService.updateSelective(parent);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    /*
     * 重命名
     * */
    @RequestMapping(method=RequestMethod.PUT)
    public ResponseEntity<Void> renameContentCategory(ContentCategory contentCategory){
       try{
           this.contentCategoryService.updateSelective(contentCategory);
           return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
       } catch(Exception e){
           e.printStackTrace();
       }
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 删除节点以及所有的子节点
     * 
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(ContentCategory contentCategory) {
        try {
            // 查找所有的子节点
            List<Object> ids = new ArrayList<Object>();
            ids.add(contentCategory.getId());
            findAllSubNode(contentCategory.getId(), ids);

            // 删除所有的子节点
            this.contentCategoryService.deleteByIds(ContentCategory.class, "id", ids);

            // 判断当前节点的父节点是否还有其他的子节点，如果没有，修改isParent为false
            ContentCategory record = new ContentCategory();
            record.setParentId(contentCategory.getParentId());
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
            if (null == list || list.isEmpty()) {
                ContentCategory parent = new ContentCategory();
                parent.setId(contentCategory.getParentId());
                parent.setIsParent(false);
                this.contentCategoryService.updateSelective(parent);
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    private void findAllSubNode(Long parentId, List<Object> ids) {
        ContentCategory record = new ContentCategory();
        record.setParentId(parentId);
        List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            // 判断该节点是否为父节点，如果是，进行递归
            if (contentCategory.getIsParent()) {
                findAllSubNode(contentCategory.getId(), ids);
            }
        }
    }
}
