package site.surui.web.student.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import site.surui.web.common.data.vo.result.Result;
import site.surui.web.student.data.dto.StudentTagRelationDto;
import site.surui.web.student.data.dto.TagDto;
import site.surui.web.student.data.vo.TagVo;
import site.surui.web.student.mapper.StudentTagRelationDtoMapper;
import site.surui.web.student.mapper.TagDtoMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagService {
    @Resource
    private TagDtoMapper tagDtoMapper;
    @Resource
    private StudentTagRelationDtoMapper relationDtoMapper;

    public Result<?> updateTags(String userId, JSONObject data) {
        boolean result = true;

        List<String> tags = (List<String>) data.get("tags");
        if (tags.isEmpty()) {
            return new Result<>().fail("Empty Content.");
        }

        for (String tag : tags) {

            if (existTag(tag)) {
                result = updateTagRelation(tag, userId);
            } else {

                TagDto newTag = new TagDto(null, tag);
                tagDtoMapper.insert(newTag);

                if (newTag.getId() == null) {
                    return new Result<>().fail();
                } else {
                    result = updateTagRelation(tag, userId);
                }
            }
        }

        return result ? new Result<>().success() : new Result<>().fail();
    }

    public Result<?> getTags(String userId) {
        List<TagVo> tags = relationDtoMapper.findAllTagsByStudent(userId);
        if (tags.isEmpty()) {
            return new Result<>().fail();
        }

        return new Result<>().success(tags);
    }

    private boolean existTag(String tag) {
        return (tagDtoMapper.existTagByName(tag) != null);
    }

    private boolean existRelation(Long id, String userId) {
        return (relationDtoMapper.existTagByIdAndUserId(id, userId) != null);
    }

    private boolean updateTagRelation(String tag, String userId) {
        // 需要考虑是否已经存在关系
        TagDto tagDto = tagDtoMapper.findByName(tag);
        if (tagDto == null) {
            return false;
        } else {
            if (existRelation(tagDto.getId(), userId)) {
                relationDtoMapper.weightIncreaseById(
                        relationDtoMapper.findRelationByIdAndUserId(tagDto.getId(), userId));
                return true;
            } else {
                StudentTagRelationDto relationDto =
                        new StudentTagRelationDto(null, userId, tagDto.getId(), null);
                relationDtoMapper.insert(relationDto);
                return relationDto.getId() != null;
            }
        }
    }
}
