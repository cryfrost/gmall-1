package com.oyyo.gmall.pms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 属性&属性分组关联
 * @author oy
 * @since 2020-04-21 16:16:49
 */
@ApiModel
@Data
@TableName("pms_attr_attrgroup_relation")
public class AttrAttrgroupRelation implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	@ApiModelProperty(name = "id",value = "id")
	private Long id;
	/**
	 * 属性id
	 */
	@ApiModelProperty(name = "attrId",value = "属性id")
	private Long attrId;
	/**
	 * 属性分组id
	 */
	@ApiModelProperty(name = "attrGroupId",value = "属性分组id")
	private Long attrGroupId;
	/**
	 * 属性组内排序
	 */
	@ApiModelProperty(name = "attrSort",value = "属性组内排序")
	private Integer attrSort;

}
