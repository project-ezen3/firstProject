package www.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public String uploadPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String homeDir = System.getProperty("user.home");
        String UP_DIR = "";
        if(os.contains("win")) {
            UP_DIR = "C:\\image\\";
        } else {
            UP_DIR = homeDir + "/image/";
        }
        return "file:" + UP_DIR;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/upload/**")
                .addResourceLocations(uploadPath());
    }
}
