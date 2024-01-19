package ghkwhd.apiServer.todo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QToDo is a Querydsl query type for ToDo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QToDo extends EntityPathBase<ToDo> {

    private static final long serialVersionUID = 960575263L;

    public static final QToDo toDo = new QToDo("toDo");

    public final BooleanPath complete = createBoolean("complete");

    public final StringPath content = createString("content");

    public final DatePath<java.time.LocalDate> dueDate = createDate("dueDate", java.time.LocalDate.class);

    public final StringPath title = createString("title");

    public final NumberPath<Long> toDoNo = createNumber("toDoNo", Long.class);

    public QToDo(String variable) {
        super(ToDo.class, forVariable(variable));
    }

    public QToDo(Path<? extends ToDo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QToDo(PathMetadata metadata) {
        super(ToDo.class, metadata);
    }

}

