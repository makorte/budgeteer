package org.wickedsource.budgeteer.web.pages.template.edit;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.templates.TemplatesPage;
import org.wickedsource.budgeteer.web.pages.templates.edit.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.templates.edit.EditTemplatePage;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class EditTemplatePageTest extends AbstractWebTestTemplate {

    @Autowired
    @ReplaceWithMock
    private TemplateService templateService;

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        tester.startPage(new EditTemplatePage(TemplatesPage.class, new PageParameters(), 1L));
        tester.assertRenderedPage(EditTemplatePage.class);
    }

    @Test
    public void testBacklink1Click() {
        WicketTester tester = getTester();
        tester.startPage(new EditTemplatePage(TemplatesPage.class, new PageParameters(), 1L));
        tester.clickLink("backlink1");
        tester.assertRenderedPage(TemplatesPage.class);
    }

    @Test
    public void testBacklink2Click() {
        WicketTester tester = getTester();
        tester.startPage(new EditTemplatePage(TemplatesPage.class, new PageParameters(), 1L));
        tester.clickLink("editForm:backlink2");
        tester.assertRenderedPage(TemplatesPage.class);
    }

    @Test
    public void NoDataFormSubmitTest(){
        WicketTester tester = getTester();
        EditTemplatePage testPage = new EditTemplatePage(TemplatesPage.class, new PageParameters(), 1L);
        tester.startPage(testPage);
        Assert.assertNotNull(testPage.get("editForm"));
        FormTester formTester = tester.newFormTester("editForm", true);
        formTester.setClearFeedbackMessagesBeforeSubmit(true);
        formTester.setFile("fileUpload", null, "");
        tester.executeAjaxEvent("editForm:save", "onclick");
        tester.assertRenderedPage(EditTemplatePage.class);
        tester.assertErrorMessages("You have not entered a name.",
                "You have not selected a type");
        assertThat(tester.getFeedbackMessages(null)).hasSize(2);
    }



    @Test
    public void AllCorrectInputForm(){
        WicketTester tester = getTester();
        EditTemplatePage testPage = new EditTemplatePage(TemplatesPage.class, new PageParameters(), 1L);
        tester.startPage(testPage);
        Assert.assertNotNull(testPage.get("editForm"));
        FormTester formTester = tester.newFormTester("editForm", true);
        formTester.setValue("name", "TEST_N");
        formTester.setValue("description", "TEST_D");
        formTester.select("type", 1);
        formTester.setFile("fileUpload", null, "");
        tester.executeAjaxEvent("editForm:save", "onclick");
        tester.assertRenderedPage(EditTemplatePage.class);
        tester.assertNoErrorMessage();
        tester.assertFeedbackMessages(null, "The template was edited successfully");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    public void NoTypeTest(){
        WicketTester tester = getTester();
        EditTemplatePage testPage = new EditTemplatePage(TemplatesPage.class, new PageParameters(), 1L);
        tester.startPage(testPage);
        Assert.assertNotNull(testPage.get("editForm"));
        FormTester formTester = tester.newFormTester("editForm", true);
        formTester.setClearFeedbackMessagesBeforeSubmit(true);
        formTester.setValue("name", "TEST_N");
        formTester.setValue("description", "TEST_D");
        tester.executeAjaxEvent("editForm:save", "onclick");
        tester.assertRenderedPage(EditTemplatePage.class);
        tester.assertErrorMessages( "You have not selected a type");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    public void DeleteButtonTest(){
        WicketTester tester = getTester();
        EditTemplatePage testPage = new EditTemplatePage(TemplatesPage.class, new PageParameters(), 1L);
        tester.startPage(testPage);
        Assert.assertNotNull(testPage.get("editForm:deleteButton"));
        tester.clickLink("editForm:deleteButton");
        tester.assertRenderedPage(DeleteDialog.class);
    }

    @Override
    public void setupTest(){
        when(templateService.getById(anyLong())).thenReturn(new Template(1L, null, null,null, null, 1L));
        when(templateService.editTemplate(anyLong(), anyLong(), any(), any())).thenReturn(1L);
    }
}
