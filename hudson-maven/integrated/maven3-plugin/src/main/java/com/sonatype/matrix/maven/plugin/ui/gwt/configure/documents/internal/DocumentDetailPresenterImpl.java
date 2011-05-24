/**
 * Sonatype Hudson Professional (TM)
 * Copyright (C) 2010-2011 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://www.sonatype.com/products/hudson/attributions/.
 * "Sonatype" and "Sonatype Hudson Professional" are trademarks of Sonatype, Inc.
 * "Hudson" is a trademark of Oracle, Inc.
 */
package com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.internal;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.sonatype.matrix.maven.model.config.DocumentAttributeDTO;
import com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.Document;
import com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.DocumentDetailPresenter;
import com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.DocumentDetailView;
import com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.DocumentManagerLiaison;
import com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.event.DocumentAddedEvent;
import com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.event.DocumentSelectedEvent;
import com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.event.DocumentUpdatedEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default implementation of {@link DocumentDetailPresenter}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.1
 */
@Singleton
public class DocumentDetailPresenterImpl
    implements DocumentDetailPresenter
{
    private final DocumentDetailView view;

    private final DocumentManagerLiaison documentManagerLiaison;

    private final ListDataProvider<DocumentAttributeDTO> attributesDataProvider;

    private Document current;

    @Inject
    public DocumentDetailPresenterImpl(final DocumentDetailView view, final DocumentManagerLiaison documentManagerLiaison) {
        this.view = checkNotNull(view);
        this.documentManagerLiaison = checkNotNull(documentManagerLiaison);

        view.setPresenter(this);

        // Hook up the data provider for attributes
        attributesDataProvider = new ListDataProvider<DocumentAttributeDTO>();
        view.getAttributesDataContainer().setSelectionModel(new NoSelectionModel<DocumentAttributeDTO>());
        attributesDataProvider.addDataDisplay(view.getAttributesDataContainer());
    }

    @Override
    public DocumentDetailView getView() {
        return view;
    }

    @Override
    public void setDocument(final Document document) {
        current = document;
        if (document != null) {
            // Fill up the form with the document details
            view.setId(document.getId());
            view.setType(document.getType());
            view.setName(document.getName());
            view.setDescription(document.getDescription());
            view.setContent(document.getContent());
            view.setNewDocument(document.isNew());
            attributesDataProvider.setList(document.getAttributes());

            // Make the view visible
            view.asWidget().setVisible(true);
        }
        else {
            // Clear the form and hide the view
            view.asWidget().setVisible(false);
        }
    }

    @Override
    public void onDocumentSelected(final DocumentSelectedEvent event) {
        checkNotNull(event);
        Document document = event.getDocument();
        Log.debug("Document selected: " + document);
        setDocument(document);
    }

    @Override
    public void onDocumentUpdated(final DocumentUpdatedEvent event) {
        checkNotNull(event);
        Document document = event.getDocument();
        Log.debug("Document updated: " + document);
        setDocument(document);
    }

    @Override
    public void onDocumentAdded(final DocumentAddedEvent event) {
        checkNotNull(event);
        Document document = event.getDocument();
        Log.debug("Document added: " + document);
        setDocument(document);
    }

    @Override
    public void doSave() {
        checkNotNull(current);

        // FIXME: Should not allow a record w/o a "name" to be saved.
        // FIXME: Need to implement some sort of form validation

        // Copy the view details into the current document
        current.setId(view.getId());
        current.setType(view.getType());
        current.setName(view.getName());
        current.setDescription(view.getDescription());
        current.setContent(view.getContent());

        // Save the document
        documentManagerLiaison.save(current);
    }

    @Override
    public void doCancel() {
        if (current == null) {
            return;
        }

        if (current.isNew()) {
            // clear temporal document and hide the view
            documentManagerLiaison.remove(current);
            view.clear();
            view.asWidget().setVisible(false);
            attributesDataProvider.getList().clear();
            current = null;
        }
        else {
            // reset the view to the current data
            setDocument(current);
        }
    }
}