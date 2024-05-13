import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IReceiptMySuffix } from 'app/entities/receipt-my-suffix/receipt-my-suffix.model';
import { ReceiptMySuffixService } from 'app/entities/receipt-my-suffix/service/receipt-my-suffix.service';
import { PaymentMySuffixService } from '../service/payment-my-suffix.service';
import { IPaymentMySuffix } from '../payment-my-suffix.model';
import { PaymentMySuffixFormService } from './payment-my-suffix-form.service';

import { PaymentMySuffixUpdateComponent } from './payment-my-suffix-update.component';

describe('PaymentMySuffix Management Update Component', () => {
  let comp: PaymentMySuffixUpdateComponent;
  let fixture: ComponentFixture<PaymentMySuffixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paymentFormService: PaymentMySuffixFormService;
  let paymentService: PaymentMySuffixService;
  let receiptService: ReceiptMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PaymentMySuffixUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PaymentMySuffixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentMySuffixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentFormService = TestBed.inject(PaymentMySuffixFormService);
    paymentService = TestBed.inject(PaymentMySuffixService);
    receiptService = TestBed.inject(ReceiptMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call receipt query and add missing value', () => {
      const payment: IPaymentMySuffix = { id: 456 };
      const receipt: IReceiptMySuffix = { id: 28976 };
      payment.receipt = receipt;

      const receiptCollection: IReceiptMySuffix[] = [{ id: 19384 }];
      jest.spyOn(receiptService, 'query').mockReturnValue(of(new HttpResponse({ body: receiptCollection })));
      const expectedCollection: IReceiptMySuffix[] = [receipt, ...receiptCollection];
      jest.spyOn(receiptService, 'addReceiptMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(receiptService.query).toHaveBeenCalled();
      expect(receiptService.addReceiptMySuffixToCollectionIfMissing).toHaveBeenCalledWith(receiptCollection, receipt);
      expect(comp.receiptsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const payment: IPaymentMySuffix = { id: 456 };
      const receipt: IReceiptMySuffix = { id: 25326 };
      payment.receipt = receipt;

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(comp.receiptsCollection).toContain(receipt);
      expect(comp.payment).toEqual(payment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaymentMySuffix>>();
      const payment = { id: 123 };
      jest.spyOn(paymentFormService, 'getPaymentMySuffix').mockReturnValue(payment);
      jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payment }));
      saveSubject.complete();

      // THEN
      expect(paymentFormService.getPaymentMySuffix).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentService.update).toHaveBeenCalledWith(expect.objectContaining(payment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaymentMySuffix>>();
      const payment = { id: 123 };
      jest.spyOn(paymentFormService, 'getPaymentMySuffix').mockReturnValue({ id: null });
      jest.spyOn(paymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payment }));
      saveSubject.complete();

      // THEN
      expect(paymentFormService.getPaymentMySuffix).toHaveBeenCalled();
      expect(paymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaymentMySuffix>>();
      const payment = { id: 123 };
      jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReceiptMySuffix', () => {
      it('Should forward to receiptService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(receiptService, 'compareReceiptMySuffix');
        comp.compareReceiptMySuffix(entity, entity2);
        expect(receiptService.compareReceiptMySuffix).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
