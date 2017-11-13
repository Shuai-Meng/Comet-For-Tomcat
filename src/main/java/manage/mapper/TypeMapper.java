package manage.mapper;

import manage.vo.MessageType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TypeMapper {
    void deleteType(@Param("id")int id);
    void updateType(MessageType messageType);
    void insertType(@Param("name")String name);
    List<MessageType> getAllTypes();
    List<MessageType> getTypeRows(Map<String, Object> map);
    int getTypeCount(@Param("name")String name);
    List<Integer> getUserIdOfType(@Param("typeId")int type);
}
