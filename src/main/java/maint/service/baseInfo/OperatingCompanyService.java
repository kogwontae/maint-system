package maint.service.baseInfo;

import java.util.List;

import maint.dto.baseInfo.operatingCompanies.OperatingCompanySelectDto;
import maint.enums.Menu;
import maint.mapper.OperatingCompaniesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 運行会社SERVICE
 *
 * @author g.ko
 */

@Service
public class OperatingCompanyService extends BaseService {

  /** FUNCTION */
  private final String FUNCTION = Menu.OP_COMPANY.getFunction();

  /** 運行会社 MAPPER */
  @Autowired
  OperatingCompaniesMapper operatingCompaniesMapper;

  /**
   * 全運行会社取得（SELECT用）
   */
  public List<OperatingCompanySelectDto> getAllOperatingCompaniesForSelectOption() {
    return operatingCompaniesMapper.getAllOperatingCompaniesForSelectOption();
  }

}

