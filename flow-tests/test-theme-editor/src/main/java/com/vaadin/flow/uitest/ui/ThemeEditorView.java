/*
 * Copyright 2000-2024 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.uitest.ui;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.uitest.servlet.ViewTestLayout;

@Route(value = "com.vaadin.flow.uitest.ui.ThemeEditorView", layout = ViewTestLayout.class)
public class ThemeEditorView extends Div {
    public ThemeEditorView() {
        Button button = new Button("Click");
        button.setId("button");
        add(new Div(button));

        Icon icon = VaadinIcon.ABACUS.create();
        icon.setId("icon");
        add(new Div(icon));

        Checkbox checkbox = new Checkbox("The checkbox");
        checkbox.setId("checkbox");
        add(new Div(checkbox));

        Accordion accordion = new Accordion();
        accordion.setId("accordion");
        accordion.add("A", new Span("Info for A"));
        accordion.add("B", new Span("Info for B"));
        add(new Div(accordion));

        Dialog dialog = new Dialog("Hey, I'm a dialog!");
        dialog.setId("dialog");
        dialog.setModal(false);
        dialog.getFooter().add(new Span("Footer"));
        dialog.add(new Span("Dialog content"));
        Button dialogButton = new Button("Open dialog", e -> dialog.open());
        dialogButton.setId("dialogButton");
        add(dialog, new Div(dialogButton));
    }
}
