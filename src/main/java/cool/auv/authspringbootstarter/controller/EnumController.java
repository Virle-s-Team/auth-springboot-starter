package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.service.EnumService;
import cool.auv.codegeneratorjpa.core.enums.ISelectable;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "获取枚举")
@RestController
@RequestMapping("/api/v1/enums")
public class EnumController {

    private final EnumService enumService;

    public EnumController(EnumService enumService) {
        this.enumService = enumService;
    }

    @GetMapping("/{enumName}")
    public ResponseEntity<List<Map<String, String>>> getEnum(@PathVariable String enumName) {
        Class<? extends Enum> clazz = enumService.findEnum(enumName);

        if (clazz == null) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, String>> list = Arrays.stream(clazz.getEnumConstants())
                .map(item -> {
                    ISelectable selectable = (ISelectable) item;
                    Enum<?> enumObj = (Enum<?>) item;
                    return Map.of("value", enumObj.name(), "label", selectable.getText());
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }
}