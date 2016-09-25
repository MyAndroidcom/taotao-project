package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.bean.Item;

//用于显示二级页面的Service 

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    @Autowired
    private RedisService redisService;

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String REDIS_KEY = "TAOTAO_WEB_ITEM_DETAIL_";

    private static final Integer REDIS_TIME = 60 * 60 * 24;

    public Item queryItemById(Long itemId) {
        // 先从缓存中获取
        String key = REDIS_KEY + itemId;
        try {
            String caheData = this.redisService.get(key);
            if (StringUtils.isNotEmpty(caheData)) {
                // 命中
                return MAPPER.readValue(caheData, Item.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String url = TAOTAO_MANAGE_URL + "/rest/item/" + itemId;
            // System.out.println("url=================================="+url);
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }

            try {
                // 将结果集写入缓存
                this.redisService.set(key, jsonData, REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
                //将结果序列化为json数据
            return MAPPER.readValue(jsonData, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    // 查找商品的描述数据
    public ItemDesc queryItemDescByItemId(Long itemId) {
        try {
            String url = TAOTAO_MANAGE_URL + "/rest/item/desc/" + itemId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            return MAPPER.readValue(jsonData, ItemDesc.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据id查询商品的规格参数
    public String queryItemParamByItemId(Long itemId) {
        try {
            String url = TAOTAO_MANAGE_URL + "rest/item/param/item/" + itemId;
            String jsonData = this.apiService.doGet(url);
//            System.out.println("url===============================---------------------------" + url);
            // 解析JSON
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            ArrayNode paramData = (ArrayNode) MAPPER.readTree(jsonNode.get("paramData").asText());
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");
            for (JsonNode param : paramData) {
                sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + param.get("group").asText()
                        + "</th></tr>");
                ArrayNode params = (ArrayNode) param.get("params");
                for (JsonNode p : params) {
                    sb.append("<tr><td class=\"tdTitle\">" + p.get("k").asText() + "</td><td>"
                            + p.get("v").asText() + "</td></tr>");
                }
            }
            sb.append("</tbody></table>");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
