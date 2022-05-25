package tableviewer;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class Main {

	public static class Person {
		public String firstname;
		public String lastname;
		public String email;

		public Person(String givenname, String surname, String email) {
			this.firstname = givenname;
			this.lastname = surname;
			this.email = email;
		}
	}

	public Main(Shell shell) {
		TableViewer viewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.setContentProvider(ArrayContentProvider.getInstance());

		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Firstname");
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				return ((Person) element).firstname;
			}
		});

		ColumnViewerComparator comparator = new ColumnViewerComparator(viewer, column) {

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				Person p1 = (Person) e1;
				Person p2 = (Person) e2;
				return p1.firstname.compareToIgnoreCase(p2.firstname) * direction;
			}
		};

		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Lastname");
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				return ((Person) element).lastname;
			}

		});

		new ColumnViewerComparator(viewer, column) {

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				Person p1 = (Person) e1;
				Person p2 = (Person) e2;
				return p1.lastname.compareToIgnoreCase(p2.lastname) * direction;
			}
		};

		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("E-Mail");
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				return ((Person) element).email;
			}
		});

		new ColumnViewerComparator(viewer, column) {

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				Person p1 = (Person) e1;
				Person p2 = (Person) e2;
				return p1.email.compareToIgnoreCase(p2.email) * direction;
			}
		};

		Person[] persons = new Person[] { 
				new Person("Tomas", "Basel", "tb@mail.com"), 
				new Person("Boris", "Beck", "bb@foo.com"),
				new Person("Teodor", "Creas", "Tod_Creas@td.com"), 
				new Person("Wayne", "Bob", "wb@eclipse.org"),
				new Person("Mario", "Candido", "mario_c@gmail.com"),
				new Person("Lars", "Meyer", "Lars.Meyerl@gmail.com"),
				new Person("Hendrik", "Forth", "hendrik.forth@gmail.com") };
		
		viewer.setInput(persons);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		viewer.setComparator(comparator);
	}

	private static abstract class ColumnViewerComparator extends ViewerComparator {

		int direction = 1;
		private TableViewerColumn column;
		private ColumnViewer viewer;

		public ColumnViewerComparator(ColumnViewer viewer, TableViewerColumn column) {
			this.column = column;
			this.viewer = viewer;
			SelectionAdapter selectionAdapter = createSelectionAdapter();
			this.column.getColumn().addSelectionListener(selectionAdapter);
		}

		private SelectionAdapter createSelectionAdapter() {
			return new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					Table table = column.getColumn().getParent();
					table.setSortColumn(column.getColumn());
					ColumnViewerComparator.this.direction = direction * -1;
					table.setSortDirection(direction == 1 ? SWT.DOWN : SWT.UP);
					viewer.refresh();
					viewer.setComparator(ColumnViewerComparator.this);
				}
			};
		}
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		new Main(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

}