/**
 * Sonatype Hudson Professional (TM)
 * Copyright (C) 2010-2011 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://www.sonatype.com/products/hudson/attributions/.
 * "Sonatype" and "Sonatype Hudson Professional" are trademarks of Sonatype, Inc.
 * "Hudson" is a trademark of Oracle, Inc.
 */
package com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.event;

import com.google.gwt.event.shared.EventHandler;
import com.sonatype.matrix.maven.plugin.ui.gwt.configure.documents.Document;

/**
 * Event fired when a document removal has completed successfully.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.1
 */
public class DocumentRemovedEvent
    extends DocumentEventSupport<DocumentRemovedEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    public DocumentRemovedEvent(final Document document) {
        super(TYPE, document);
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onDocumentRemoved(this);
    }

    public static interface Handler
        extends EventHandler
    {
        void onDocumentRemoved(DocumentRemovedEvent event);
    }
}