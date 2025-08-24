package maint.mapper;

import java.util.List;

import maint.dto.baseInfo.operatingCompanies.OperatingCompanySelectDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 運行会社MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface OperatingCompaniesMapper {

  /**
   * 全運行会社取得（SELECT用）
   *
   * @return 運行会社リスト
   */
  List<OperatingCompanySelectDto> getAllOperatingCompaniesForSelectOption();

}

