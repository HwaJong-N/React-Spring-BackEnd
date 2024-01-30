package ghkwhd.apiServer.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 1310233881L;

    public static final QItem item = new QItem("item");

    public final BooleanPath delFlag = createBoolean("delFlag");

    public final ListPath<ItemImage, QItemImage> imageList = this.<ItemImage, QItemImage>createList("imageList", ItemImage.class, QItemImage.class, PathInits.DIRECT2);

    public final StringPath itemDesc = createString("itemDesc");

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    public final StringPath itemName = createString("itemName");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public QItem(String variable) {
        super(Item.class, forVariable(variable));
    }

    public QItem(Path<? extends Item> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItem(PathMetadata metadata) {
        super(Item.class, metadata);
    }

}

