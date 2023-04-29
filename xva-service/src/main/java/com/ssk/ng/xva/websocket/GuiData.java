package com.ssk.ng.xva.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GuiData {
    private String updateType;
    private String dataType;
    private Object payload;
}
