package xiaozhi.modules.agent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import xiaozhi.modules.agent.service.AgentService;
import xiaozhi.modules.agent.dto.*;
import xiaozhi.modules.agent.entity.AgentEntity;
import xiaozhi.modules.agent.vo.*;
import xiaozhi.common.page.PageData;
import xiaozhi.common.service.impl.BaseServiceImpl;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * DB接続失敗時の代替AgentService実装
 */
@Service
@ConditionalOnMissingBean(name = "agentServiceImpl")
public class SimpleAgentServiceImpl extends BaseServiceImpl<Object, AgentEntity> implements AgentService {

    @Override
    public PageData<AgentEntity> adminAgentList(Map<String, Object> params) {
        return new PageData<>(new ArrayList<>(), 0L);
    }

    @Override
    public boolean insert(AgentEntity entity) {
        return true;
    }

    @Override
    public void deleteAgentByUserId(Long userId) {
        // No operation
    }

    @Override
    public List<AgentDTO> getUserAgents(Long userId) {
        return new ArrayList<>();
    }

    @Override
    public Integer getDeviceCountByAgentId(String agentId) {
        return 0;
    }

    @Override
    public AgentEntity getDefaultAgentByMacAddress(String macAddress) {
        AgentEntity entity = new AgentEntity();
        entity.setId("default");
        entity.setAgentName("Default Agent");
        return entity;
    }

    @Override
    public AgentInfoVO getAgentById(String id) {
        AgentInfoVO vo = new AgentInfoVO();
        vo.setId(id);
        vo.setName("Default Agent");
        return vo;
    }

    @Override
    public String save(AgentCreateDTO dto) {
        return "default-agent-id";
    }

    @Override
    public void update(String id, AgentUpdateDTO dto) {
        // No operation
    }

    @Override
    public void delete(String id) {
        // No operation
    }

    @Override
    public List<AgentChatHistoryDTO> getAgentChatHistory(String agentId, int page, int limit, String keyword) {
        return new ArrayList<>();
    }

    @Override
    public List<AgentChatHistoryUserVO> getRecentlyFiftyByAgentId(String agentId) {
        return new ArrayList<>();
    }

    @Override
    public String getAudioId(String audioId) {
        return audioId;
    }

    @Override
    public boolean checkAgentPermission(String agentId, Long userId) {
        return true; // 認証なしモードでは全て許可
    }

    @Override
    public void updateAgentById(String agentId, AgentUpdateDTO dto) {
        // No operation
    }

    @Override
    public String createAgent(AgentCreateDTO dto) {
        return "default-agent-id";
    }
}
