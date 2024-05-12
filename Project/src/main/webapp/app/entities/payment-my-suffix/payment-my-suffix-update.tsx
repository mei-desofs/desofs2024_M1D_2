import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReceiptMySuffix } from 'app/shared/model/receipt-my-suffix.model';
import { getEntities as getReceipts } from 'app/entities/receipt-my-suffix/receipt-my-suffix.reducer';
import { IPaymentMySuffix } from 'app/shared/model/payment-my-suffix.model';
import { getEntity, updateEntity, createEntity, reset } from './payment-my-suffix.reducer';

export const PaymentMySuffixUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const receipts = useAppSelector(state => state.receipt.entities);
  const paymentEntity = useAppSelector(state => state.payment.entity);
  const loading = useAppSelector(state => state.payment.loading);
  const updating = useAppSelector(state => state.payment.updating);
  const updateSuccess = useAppSelector(state => state.payment.updateSuccess);

  const handleClose = () => {
    navigate('/payment-my-suffix');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReceipts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.idCart !== undefined && typeof values.idCart !== 'number') {
      values.idCart = Number(values.idCart);
    }

    const entity = {
      ...paymentEntity,
      ...values,
      receipt: receipts.find(it => it.id.toString() === values.receipt?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...paymentEntity,
          receipt: paymentEntity?.receipt?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="onlineShopApp.payment.home.createOrEditLabel" data-cy="PaymentCreateUpdateHeading">
            Create or edit a Payment
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="payment-my-suffix-id" label="Id" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Id Cart" id="payment-my-suffix-idCart" name="idCart" data-cy="idCart" type="text" />
              <ValidatedField label="Date" id="payment-my-suffix-date" name="date" data-cy="date" type="date" />
              <ValidatedField id="payment-my-suffix-receipt" name="receipt" data-cy="receipt" label="Receipt" type="select">
                <option value="" key="0" />
                {receipts
                  ? receipts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/payment-my-suffix" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PaymentMySuffixUpdate;
