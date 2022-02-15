package com.oauth.serviceauth.config;

import com.oauth.serviceauth.customImpl.MyRedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * @author:GSHG
 * @date: 2021-11-09 6:21 PM
 * description: 用于配置授权服务器端点的安全过滤拦截
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    /**
     *     该对象用来支持 password 模式
      */
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     *     将令牌信息村粗到redis中
     */
    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    /**
     * 用来配置客户端详情服务（ClientDetailsService）
     ，客户端详情信息在这里进行初始化，
     能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息；
     配置授权码模式
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
        // 指定密码的加密方式
        String finalSecret = "{bcrypt}"+new BCryptPasswordEncoder().encode("123456");

        //配置两个客户端，一个用于password认证，1个用于client认证
        clients.inMemory().withClient("client_1")
        .authorizedGrantTypes("client_credentials","refresh_token")
                //                .resourceIds(Utils.RESOURCEIDS.ORDER)
                .scopes("select")
                .authorities("oauth2")
                .secret(finalSecret)
                .and().withClient("client_2")
                //                .resourceIds(Utils.RESOURCEIDS.ORDER)
                .authorizedGrantTypes("password","refresh_token")
                .scopes("server")
                .authorities("oauth2")
                .secret(finalSecret);

    }

    /**
     *     configure(AuthorizationServerEndpointsConfigurer endpoints) 用来配置授权（authorization）
     *     以及令牌（token）的访问端点和令牌服务(token services)，还有token的存储方式(tokenStore)；
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception{
        //配置令牌的存储（这里存放在redis中）
    endpoints.tokenStore(new MyRedisTokenStore(redisConnectionFactory))
            .authenticationManager(authenticationManager)
            //默认不支持GET请求，这里配置后可支持GET和POST请求
            .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);
    }

    /** 检测token策略
     * configure(AuthorizationServerSecurityConfigurer security) 用来配置令牌端点(Token Endpoint)的安全约束；
     * @param security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        //// 表示支持 client_id 和 client_secret 做登录认证
        //允许表单认证
        security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()");
                //.checkTokenAccess("isAuthenticated()");

    }

}
