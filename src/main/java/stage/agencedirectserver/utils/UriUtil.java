package stage.agencedirectserver.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class UriUtil {
    public static URI Uri(String path) {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(path).toUriString());
    }
}
