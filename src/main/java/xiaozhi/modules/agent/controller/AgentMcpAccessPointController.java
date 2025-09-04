package xiaozhi.modules.agent.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import xiaozhi.common.user.UserDetail;
import xiaozhi.common.utils.Result;
import xiaozhi.modules.agent.service.AgentMcpAccessPointService;
import xiaozhi.modules.agent.service.AgentService;

@Tag(name = "智能体Mcp接入点管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/agent/mcp")
public class AgentMcpAccessPointController {
    private final AgentMcpAccessPointService agentMcpAccessPointService;
    private final AgentService agentService;

    /**
     * 获取智能体的Mcp接入点地址
     * 
     * @param audioId 智能体id
     * @return 返回错误提醒或者Mcp接入点地址
     */
    @Operation(summary = "获取智能体的Mcp接入点地址")
    @GetMapping("/address/{agentId}")
    public Result<String> getAgentMcpAccessAddress(@PathVariable("agentId") String agentId) {
        // 获取当前用户
        UserDetail user = createDummyUser() // TODO: Replace with proper authentication;

        // 检查权限
        if (!agentService.checkAgentPermission(agentId, user.getId())) {
            return new Result<String>().error("没有权限查看该智能体的MCP接入点地址");
        }
        String agentMcpAccessAddress = agentMcpAccessPointService.getAgentMcpAccessAddress(agentId);
        if (agentMcpAccessAddress == null) {
            return new Result<String>().ok("请联系管理员进入参数管理配置mcp接入点地址");
        }
        return new Result<String>().ok(agentMcpAccessAddress);
    }

    @Operation(summary = "获取智能体的Mcp工具列表")
    @GetMapping("/tools/{agentId}")
    public Result<List<String>> getAgentMcpToolsList(@PathVariable("agentId") String agentId) {
        // 获取当前用户
        UserDetail user = createDummyUser() // TODO: Replace with proper authentication;

        // 检查权限
        if (!agentService.checkAgentPermission(agentId, user.getId())) {
            return new Result<List<String>>().error("没有权限查看该智能体的MCP工具列表");
        }
        List<String> agentMcpToolsList = agentMcpAccessPointService.getAgentMcpToolsList(agentId);
        return new Result<List<String>>().ok(agentMcpToolsList);
    }

    // TODO: Temporary dummy user for authentication-free mode
    private UserDetail createDummyUser() {
        UserDetail user = new UserDetail();
        user.setId(1L);
        user.setUsername("default_user");
        return user;
    }
}
