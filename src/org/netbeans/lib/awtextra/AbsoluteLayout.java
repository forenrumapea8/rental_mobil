package org.netbeans.lib.awtextra;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class AbsoluteLayout implements LayoutManager2, Serializable {
    private final Map<Component, AbsoluteConstraints> constraints = new LinkedHashMap<>();

    public void addLayoutComponent(String name, Component comp) { addLayoutComponent(comp, new AbsoluteConstraints(0,0,-1,-1)); }
    public void addLayoutComponent(Component comp, Object cons) {
        if (cons instanceof AbsoluteConstraints) constraints.put(comp, (AbsoluteConstraints) cons);
        else constraints.put(comp, new AbsoluteConstraints(0,0,-1,-1));
    }
    public void removeLayoutComponent(Component comp) { constraints.remove(comp); }
    public Dimension preferredLayoutSize(Container parent) { return calc(parent); }
    public Dimension minimumLayoutSize(Container parent) { return calc(parent); }
    public Dimension maximumLayoutSize(Container target) { return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE); }
    public float getLayoutAlignmentX(Container target) { return 0.5f; }
    public float getLayoutAlignmentY(Container target) { return 0.5f; }
    public void invalidateLayout(Container target) {}

    private Dimension calc(Container parent) {
        Insets ins = parent.getInsets(); int maxX = 0, maxY = 0;
        for (Map.Entry<Component, AbsoluteConstraints> e: constraints.entrySet()) {
            Component c = e.getKey(); AbsoluteConstraints a = e.getValue();
            Dimension d = c.getPreferredSize();
            int w = a.width == -1 ? d.width : a.width;
            int h = a.height == -1 ? d.height : a.height;
            maxX = Math.max(maxX, a.x + w); maxY = Math.max(maxY, a.y + h);
        }
        return new Dimension(maxX + ins.left + ins.right, maxY + ins.top + ins.bottom);
    }
    public void layoutContainer(Container parent) {
        Insets ins = parent.getInsets();
        for (Map.Entry<Component, AbsoluteConstraints> e: constraints.entrySet()) {
            Component c = e.getKey(); AbsoluteConstraints a = e.getValue();
            Dimension d = c.getPreferredSize();
            int w = a.width == -1 ? d.width : a.width;
            int h = a.height == -1 ? d.height : a.height;
            c.setBounds(ins.left + a.x, ins.top + a.y, w, h);
        }
    }
}
