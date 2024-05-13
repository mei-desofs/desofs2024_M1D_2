import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPaymentMySuffix } from 'app/entities/payment-my-suffix/payment-my-suffix.model';
import { PaymentMySuffixService } from 'app/entities/payment-my-suffix/service/payment-my-suffix.service';
import { CartMySuffixService } from '../service/cart-my-suffix.service';
import { ICartMySuffix } from '../cart-my-suffix.model';
import { CartMySuffixFormService } from './cart-my-suffix-form.service';

import { CartMySuffixUpdateComponent } from './cart-my-suffix-update.component';

describe('CartMySuffix Management Update Component', () => {
  let comp: CartMySuffixUpdateComponent;
  let fixture: ComponentFixture<CartMySuffixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cartFormService: CartMySuffixFormService;
  let cartService: CartMySuffixService;
  let paymentService: PaymentMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CartMySuffixUpdateComponent],
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
      .overrideTemplate(CartMySuffixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CartMySuffixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cartFormService = TestBed.inject(CartMySuffixFormService);
    cartService = TestBed.inject(CartMySuffixService);
    paymentService = TestBed.inject(PaymentMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call payment query and add missing value', () => {
      const cart: ICartMySuffix = { id: 456 };
      const payment: IPaymentMySuffix = { id: 31315 };
      cart.payment = payment;

      const paymentCollection: IPaymentMySuffix[] = [{ id: 9621 }];
      jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
      const expectedCollection: IPaymentMySuffix[] = [payment, ...paymentCollection];
      jest.spyOn(paymentService, 'addPaymentMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cart });
      comp.ngOnInit();

      expect(paymentService.query).toHaveBeenCalled();
      expect(paymentService.addPaymentMySuffixToCollectionIfMissing).toHaveBeenCalledWith(paymentCollection, payment);
      expect(comp.paymentsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cart: ICartMySuffix = { id: 456 };
      const payment: IPaymentMySuffix = { id: 21439 };
      cart.payment = payment;

      activatedRoute.data = of({ cart });
      comp.ngOnInit();

      expect(comp.paymentsCollection).toContain(payment);
      expect(comp.cart).toEqual(cart);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICartMySuffix>>();
      const cart = { id: 123 };
      jest.spyOn(cartFormService, 'getCartMySuffix').mockReturnValue(cart);
      jest.spyOn(cartService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cart });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cart }));
      saveSubject.complete();

      // THEN
      expect(cartFormService.getCartMySuffix).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cartService.update).toHaveBeenCalledWith(expect.objectContaining(cart));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICartMySuffix>>();
      const cart = { id: 123 };
      jest.spyOn(cartFormService, 'getCartMySuffix').mockReturnValue({ id: null });
      jest.spyOn(cartService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cart: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cart }));
      saveSubject.complete();

      // THEN
      expect(cartFormService.getCartMySuffix).toHaveBeenCalled();
      expect(cartService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICartMySuffix>>();
      const cart = { id: 123 };
      jest.spyOn(cartService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cart });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cartService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePaymentMySuffix', () => {
      it('Should forward to paymentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(paymentService, 'comparePaymentMySuffix');
        comp.comparePaymentMySuffix(entity, entity2);
        expect(paymentService.comparePaymentMySuffix).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
