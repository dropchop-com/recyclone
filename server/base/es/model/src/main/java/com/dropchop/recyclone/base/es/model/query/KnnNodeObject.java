package com.dropchop.recyclone.base.es.model.query;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.KnnCondition;
import com.dropchop.recyclone.base.api.model.query.condition.Knn;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class KnnNodeObject extends QueryNodeObject {
  private final String field;
  private final float[] queryVector;
  private final String queryString;
  private final Integer k;
  private final Condition filter;
  private final Float similarity;
  private final Integer numCandidates;

  private void validateAndBuild() {
    if (field == null || field.isEmpty()) {
      throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "kNN query must have a field specified"
      );
    }

    if (queryVector != null && queryVector.length > 0) {
      this.put("query_vector", queryVector);
    } else if (queryString != null && !queryString.isBlank()) {
      this.put("query_vector", new float[]{});
    } else {
      throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "kNN query must have query_vector or query_string specified"
      );
    }

    this.put("field", field);

    if (k != null) {
      this.put("k", k);
    }

    if (similarity != null) {
      this.put("similarity", similarity);
    }

    if (numCandidates != null) {
      this.put("num_candidates", numCandidates);
    }
  }

  public KnnNodeObject(IQueryNode parent, Knn knnQuery) {
    super(parent);
    this.field = knnQuery.get$knn().getName();
    KnnCondition<?> condition = knnQuery.get$knn();
    Object value = condition.getValue();
    if (value instanceof float[] floats) {
      this.queryVector = floats;
      this.queryString = null;
    } else if (value instanceof String str) {
      this.queryVector = null;
      this.queryString = str;
    } else {
      this.queryVector = null;
      this.queryString = null;
    }
    this.k = condition.getTopK();
    this.filter = condition.getFilter();
    this.similarity = condition.getSimilarity();
    this.numCandidates = condition.getNumCandidates();

    validateAndBuild();
  }

  public void replaceQueryVector(float[] queryVector) {
    if (queryVector == null || queryVector.length == 0) {
      this.remove("query_vector");
    } else {
      this.put("query_vector", queryVector);
    }
  }

  public void removeQueryVector() {
    replaceQueryVector(null);
  }

  public void renameField(String field) {
    this.put("field", field);
  }

  public void replaceSimilarity(Float similarity) {
    if (similarity == null) {
      this.remove("similarity");
      return;
    }
    this.put("similarity", similarity);
  }

  public void replaceTopK(Integer topK) {
    if (topK == null) {
      this.remove("k");
      return;
    }
    this.put("k", topK);
  }

  public void replaceNumCandidates(Integer numCandidates) {
    if (numCandidates == null) {
      this.remove("num_candidates");
      return;
    }
    this.put("num_candidates", numCandidates);
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
