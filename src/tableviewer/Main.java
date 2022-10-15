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
import org.eclipse.swt.widgets.TableColumn;

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
		TableViewer tv = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		tv.setContentProvider(ArrayContentProvider.getInstance());

		TableViewerColumn tvc1 = new TableViewerColumn(tv, SWT.NONE);
		tvc1.getColumn().setWidth(200);
		tvc1.getColumn().setText("Firstname");
		tvc1.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Person) element).firstname;
			}
		});

		ColumnViewerComparator comparator = new ColumnViewerComparator(tv, tvc1) {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				Person p1 = (Person) e1;
				Person p2 = (Person) e2;
				return p1.firstname.compareToIgnoreCase(p2.firstname) * direction;
			}
		};

		TableViewerColumn tvc2 = new TableViewerColumn(tv, SWT.NONE);
		tvc2.getColumn().setWidth(200);
		tvc2.getColumn().setText("Lastname");
		tvc2.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Person) element).lastname;
			}
		});

		new ColumnViewerComparator(tv, tvc2) {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				Person p1 = (Person) e1;
				Person p2 = (Person) e2;
				return p1.lastname.compareToIgnoreCase(p2.lastname) * direction;
			}
		};

		TableViewerColumn tvc3 = new TableViewerColumn(tv, SWT.NONE);
		tvc3.getColumn().setWidth(200);
		tvc3.getColumn().setText("E-Mail");
		tvc3.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Person) element).email;
			}
		});

		new ColumnViewerComparator(tv, tvc3) {
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
			new Person("Hendrik", "Forth", "hendrik.forth@gmail.com") 
		};

		tv.setInput(persons);
		tv.getTable().setLinesVisible(true);
		tv.getTable().setHeaderVisible(true);
		tv.setComparator(comparator);
	}

	static class ColumnViewerComparator extends ViewerComparator {
		int direction = 1;
		private TableViewerColumn tvc;
		private ColumnViewer cv;

		public ColumnViewerComparator(ColumnViewer cv, TableViewerColumn tvc) {
			this.cv = cv;
			this.tvc = tvc;
			getColumn().addSelectionListener(createSelectionAdapter(this));
		}

		private TableColumn getColumn() {
			return tvc.getColumn();
		}

		private void changeDirection() {
			direction *= -1;
		}

		private SelectionAdapter createSelectionAdapter(ColumnViewerComparator parent) {
			return new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Table table = tvc.getColumn().getParent();
					table.setSortColumn(tvc.getColumn());
					changeDirection();
					table.setSortDirection(direction == 1 ? SWT.DOWN : SWT.UP);
					cv.setComparator(parent);
					cv.refresh();
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