package manage.mapper;

import manage.vo.MessageType;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface TypeMapper extends Mapper<MessageType> {
}
