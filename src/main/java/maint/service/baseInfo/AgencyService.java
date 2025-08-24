package maint.service.baseInfo;

import java.util.List;
import maint.dto.baseInfo.agencies.AgencySelectDto;
import maint.mapper.AgenciesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 事業者SERVICE
 *
 * @author g.ko
 */

@Service
public class AgencyService extends BaseService {

  /** 事業者 MAPPER */
  @Autowired
  AgenciesMapper agenciesMapper;

  /**
   * 全事業者取得（SELECT用）
   *
   * @return 事業者DTOリスト
   */
  public List<AgencySelectDto> getAllAgenciesForSelectOption() {
    return agenciesMapper.getAllAgenciesForSelectOption();
  }

}

