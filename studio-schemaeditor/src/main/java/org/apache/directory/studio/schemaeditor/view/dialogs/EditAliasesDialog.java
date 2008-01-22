/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */

package org.apache.directory.studio.schemaeditor.view.dialogs;


import java.util.ArrayList;
import java.util.List;

import org.apache.directory.studio.schemaeditor.Activator;
import org.apache.directory.studio.schemaeditor.PluginUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;


/**
 * This class implements the Manage Aliases Dialog.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class EditAliasesDialog extends Dialog
{
    /** The aliases List */
    private List<String> initialLowerCasedAliases;
    private List<String> aliases;
    private List<String> lowerCasedAliases;

    /** The dirty flag */
    private boolean dirty = false;

    /** The listener used to override the listerner on the RETURN key */
    private Listener returnKeyListener = new Listener()
    {
        public void handleEvent( Event event )
        {
            if ( event.detail == SWT.TRAVERSE_RETURN )
            {
                event.detail = SWT.TRAVERSE_TAB_NEXT;
                closeTableEditor();
            }
        }
    };

    /** The {@link Display} */
    private Display display;

    // UI Fields
    private Table aliasesTable;
    private TableEditor tableEditor;
    private Button addButton;
    private Button editButton;
    private Button removeButton;
    private Composite errorComposite;
    private Image errorImage;
    private Label errorLabel;


    /**
     * Creates a new instance of EditAliasesDialog.
     *
     * @param aliases
     *      an array containing the aliases
     */
    public EditAliasesDialog( String[] aliases )
    {
        super( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() );
        initialLowerCasedAliases = new ArrayList<String>();
        this.aliases = new ArrayList<String>();
        lowerCasedAliases = new ArrayList<String>();
        if ( aliases != null )
        {
            for ( String alias : aliases )
            {
                initialLowerCasedAliases.add( alias.toLowerCase() );
                this.aliases.add( alias );
                lowerCasedAliases.add( alias.toLowerCase() );
            }
        }

        display = Activator.getDefault().getWorkbench().getDisplay();
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea( Composite parent )
    {
        // Creating the composite
        Composite composite = new Composite( parent, SWT.NONE );
        composite.setLayout( new GridLayout( 2, false ) );
        composite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

        // Aliases Label
        Label aliasesLabel = new Label( composite, SWT.NONE );
        aliasesLabel.setText( "Aliases:" );
        aliasesLabel.setLayoutData( new GridData( SWT.FILL, SWT.NONE, true, true, 2, 1 ) );

        // Aliases Table
        aliasesTable = new Table( composite, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
            | SWT.HIDE_SELECTION );
        GridData gridData = new GridData( SWT.FILL, SWT.FILL, true, true, 1, 3 );
        gridData.heightHint = 125;
        gridData.minimumHeight = 125;
        gridData.widthHint = 200;
        gridData.minimumWidth = 200;
        aliasesTable.setLayoutData( gridData );

        // Aliases Table Editor
        tableEditor = new TableEditor( aliasesTable );
        tableEditor.horizontalAlignment = SWT.LEFT;
        tableEditor.grabHorizontal = true;
        tableEditor.minimumWidth = 200;

        // Add Button
        addButton = new Button( composite, SWT.PUSH );
        addButton.setText( "Add..." );
        addButton.setLayoutData( new GridData( SWT.FILL, SWT.NONE, false, false ) );

        // Edit Button
        editButton = new Button( composite, SWT.PUSH );
        editButton.setText( "Edit..." );
        editButton.setLayoutData( new GridData( SWT.FILL, SWT.NONE, false, false ) );
        editButton.setEnabled( false );

        // Remove Button
        removeButton = new Button( composite, SWT.PUSH );
        removeButton.setText( "Remove" );
        removeButton.setLayoutData( new GridData( SWT.FILL, SWT.NONE, false, false ) );
        removeButton.setEnabled( false );

        // Error Composite
        errorComposite = new Composite( composite, SWT.NONE );
        errorComposite.setLayout( new GridLayout( 2, false ) );
        errorComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );
        errorComposite.setVisible( false );

        // Error Image
        errorImage = PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJS_ERROR_TSK );
        Label label = new Label( errorComposite, SWT.NONE );
        label.setImage( errorImage );
        label.setSize( 16, 16 );

        // Error Label
        errorLabel = new Label( errorComposite, SWT.NONE );
        errorLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
        errorLabel.setText( "An element with the same alias already exists." );

        // Filling the Table with the given aliases
        fillAliasesTable();

        // Listeners initialization
        initListeners();

        return composite;
    }


    /**
     * Fills in the Aliases Table from the aliases list 
     */
    private void fillAliasesTable()
    {
        aliasesTable.removeAll();
        aliasesTable.setItemCount( 0 );
        for ( String alias : aliases )
        {
            TableItem newItem = new TableItem( aliasesTable, SWT.NONE );
            newItem.setText( alias );
        }
    }


    /**
     * Initializes the Listeners.
     */
    private void initListeners()
    {
        aliasesTable.addKeyListener( new KeyAdapter()
        {
            public void keyPressed( KeyEvent e )
            {
                if ( ( e.keyCode == SWT.DEL ) || ( e.keyCode == Action.findKeyCode( "BACKSPACE" ) ) ) //$NON-NLS-1$
                {
                    removeSelectedAliases();
                    fillAliasesTable();
                    updateButtonsState();
                    checkAliases();
                }
            }
        } );
        aliasesTable.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                closeTableEditor();
                updateButtonsState();
            }
        } );
        aliasesTable.addListener( SWT.MouseDoubleClick, new Listener()
        {
            public void handleEvent( Event event )
            {
                openTableEditor( aliasesTable.getItem( aliasesTable.getSelectionIndex() ) );
            }
        } );

        // Aliases Table's Popup Menu
        Menu menu = new Menu( getShell(), SWT.POP_UP );
        aliasesTable.setMenu( menu );
        MenuItem removeMenuItem = new MenuItem( menu, SWT.PUSH );
        removeMenuItem.setText( "Remove" );
        removeMenuItem.setImage( PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_TOOL_DELETE ) );
        removeMenuItem.addListener( SWT.Selection, new Listener()
        {
            public void handleEvent( Event event )
            {
                removeSelectedAliases();
                fillAliasesTable();
                updateButtonsState();
                checkAliases();
            }
        } );

        // Add Button
        addButton.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                addANewAlias();
            }
        } );

        // Edit Button
        editButton.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                openTableEditor( aliasesTable.getItem( aliasesTable.getSelectionIndex() ) );
            }
        } );

        // Remove Button
        removeButton.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                removeSelectedAliases();
                fillAliasesTable();
                updateButtonsState();
                checkAliases();
            }
        } );
    }


    /**
     * Updates the state of the buttons.
     */
    private void updateButtonsState()
    {
        if ( aliasesTable.getSelectionCount() >= 1 )
        {
            editButton.setEnabled( true );
            removeButton.setEnabled( true );
        }
        else
        {
            editButton.setEnabled( false );
            removeButton.setEnabled( false );
        }
    }


    /**
     * Removes the selected aliases.
     */
    private void removeSelectedAliases()
    {
        TableItem[] selectedItems = aliasesTable.getSelection();
        for ( TableItem item : selectedItems )
        {
            aliases.remove( item.getText() );
            lowerCasedAliases.remove( item.getText().toLowerCase() );
        }
        dirty = true;
    }


    /**
     * Adds a new alias
     */
    private void addANewAlias()
    {
        TableItem item = new TableItem( aliasesTable, SWT.NONE );
        item.setText( "" );
        openTableEditor( item );
        dirty = true;
    }


    /**
     * Opens the {@link TableEditor} on the given {@link TableItem}.
     *
     * @param item
     *      the {@link TableItem}
     */
    private void openTableEditor( TableItem item )
    {
        // Clean up any previous editor control
        Control oldEditor = tableEditor.getEditor();
        if ( oldEditor != null )
            oldEditor.dispose();

        if ( item == null )
            return;

        // The control that will be the editor must be a child of the Table
        Text newEditor = new Text( aliasesTable, SWT.NONE );
        newEditor.setText( item.getText() );
        newEditor.addModifyListener( new ModifyListener()
        {
            public void modifyText( ModifyEvent e )
            {
                saveTableEditorText();
            }
        } );
        newEditor.addKeyListener( new KeyAdapter()
        {
            public void keyPressed( KeyEvent e )
            {
                if ( ( e.keyCode == Action.findKeyCode( "RETURN" ) ) || ( e.keyCode == SWT.KEYPAD_CR ) )
                {
                    closeTableEditor();
                }
            }
        } );
        newEditor.selectAll();
        newEditor.setFocus();
        tableEditor.setEditor( newEditor, item, 0 );
        display.addFilter( SWT.Traverse, returnKeyListener );
    }


    /**
     * Saves the {@link TableEditor} text.
     */
    private void saveTableEditorText()
    {
        Text text = ( Text ) tableEditor.getEditor();
        if ( text != null )
        {
            TableItem item = tableEditor.getItem();
            String oldText = item.getText();
            String newText = text.getText();
            if ( !oldText.equals( newText ) )
            {
                aliases.remove( oldText );
                lowerCasedAliases.remove( oldText.toLowerCase() );
                if ( !newText.equals( "" ) )
                {
                    aliases.add( newText );
                    lowerCasedAliases.add( newText.toLowerCase() );
                }
                item.setText( newText );
                dirty = true;
            }
        }
        checkAliases();
    }


    /**
     * Closes the {@link TableEditor}.
     */
    private void closeTableEditor()
    {
        Text text = ( Text ) tableEditor.getEditor();
        if ( text != null )
        {
            saveTableEditorText();
            text.dispose();
        }
        display.removeFilter( SWT.Traverse, returnKeyListener );
    }


    /**
     * Checks the aliases.
     */
    private void checkAliases()
    {
        errorComposite.setVisible( false );

        for ( String alias : aliases )
        {
            if ( ( Activator.getDefault().getSchemaHandler().isAliasOrOidAlreadyTaken( alias ) )
                && ( !initialLowerCasedAliases.contains( alias.toLowerCase() ) ) )
            {
                errorComposite.setVisible( true );
                errorLabel.setText( "An element with the same alias already exists." );
            }
            else if ( !PluginUtils.verifyName( alias ) )
            {
                errorComposite.setVisible( true );
                errorLabel.setText( "The alias '" + alias + "' is invalid." );
            }
        }
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell( Shell newShell )
    {
        super.configureShell( newShell );
        newShell.setText( "Edit Aliases" );
    }


    /**
     * Returns the aliases.
     *  
     * @return
     *      the aliases
     */
    public String[] getAliases()
    {
        return aliases.toArray( new String[0] );
    }


    /**
     * Gets the Dirty flag of the dialog
     *
     * @return
     *      the dirty flag of the dialog
     */
    public boolean isDirty()
    {
        return dirty;
    }
}
