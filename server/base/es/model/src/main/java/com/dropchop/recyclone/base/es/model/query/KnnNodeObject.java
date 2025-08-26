package com.dropchop.recyclone.base.es.model.query;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.knn.Knn;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class KnnNodeObject extends QueryNodeObject {
  private final String field;
  private final float[] queryVector;
  private final Integer k;
  private final Condition filter;
  private final Float similarity;

  public KnnNodeObject(IQueryNode parent, Knn knnQuery) {
    super(parent);
    this.field = knnQuery.getField();
    this.queryVector = knnQuery.getQueryVector();
    this.k = knnQuery.getK();
    this.filter = knnQuery.getFilter();
    this.similarity = knnQuery.getSimilarity();

    validateAndBuild();
  }

  private void validateAndBuild() {
    if (field == null || field.isEmpty()) {
      throw new ServiceException(
        ErrorCode.parameter_validation_error,
        "kNN query must have a field specified"
      );
    }

    if (queryVector == null || queryVector.length == 0) {
      throw new ServiceException(
        ErrorCode.parameter_validation_error,
        "kNN query must have query_vector"
      );
    }

    this.put("field", field);
    this.put("query_vector", queryVector);

    if (k != null) {
      this.put("k", k);
    }

    if (similarity != null) {
      this.put("similarity", similarity);
    }
  }

  public KnnNodeObject addFilter(QueryNodeObject filterQuery) {
    if (filter != null && filterQuery != null) {
      QueryNodeObject wrappedFilter = new QueryNodeObject(this);
      wrappedFilter.put("bool", filterQuery);
      this.put("filter", wrappedFilter);
    }
    return this;
  }

  @Override
  public void setParent(IQueryNode parent) {
    IQueryNode prevParent = this.getParent();
    if (prevParent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) prevParent).remove("knn");
    }
    super.setParent(parent);
    if (parent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) parent).put("knn", this);
    }
  }
}
