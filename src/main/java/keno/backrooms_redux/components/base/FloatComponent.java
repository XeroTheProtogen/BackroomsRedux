package keno.backrooms_redux.components.base;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface FloatComponent extends Component {
    float getValue();
    void setValue(float value);
}
