package maint.mapper;

import java.util.List;
import maint.dto.baseInfo.agencies.AgencySelectDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 事業者MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface AgenciesMapper {

  /**
   * 全事業者取得（SELECT用）
   *
   * @return 全事業者
   */
  List<AgencySelectDto> getAllAgenciesForSelectOption();

  /**
   * 事業者名取得
   *
   * @param businessCorpId 事業者ID
   * @return 事業者名
   */
  String getBusinessCorpNameByBusinessCorpId(Long businessCorpId);

}
