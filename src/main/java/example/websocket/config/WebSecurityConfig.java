package example.websocket.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


// auto sign in

//@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()// 自訂授權規則
                .antMatchers("/*")//傳入 HTTP 請求方法與 API 路徑 : 後面接著授權方式 「 "*" 」：代表0到多個字元
                .permitAll()// 允許所有請求
                .and()
                .csrf().disable();// 關閉對 CSRF（跨站請求偽造）攻擊的防護。這樣 Security 機制才不會拒絕外部直接對 API 發出的請求，如 Postman 與前端。
    }
}
