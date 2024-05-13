import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IPaymentMySuffix } from '../payment-my-suffix.model';
import { PaymentMySuffixService } from '../service/payment-my-suffix.service';

import paymentResolve from './payment-my-suffix-routing-resolve.service';

describe('PaymentMySuffix routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: PaymentMySuffixService;
  let resultPaymentMySuffix: IPaymentMySuffix | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(PaymentMySuffixService);
    resultPaymentMySuffix = undefined;
  });

  describe('resolve', () => {
    it('should return IPaymentMySuffix returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        paymentResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPaymentMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentMySuffix).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        paymentResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPaymentMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPaymentMySuffix).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPaymentMySuffix>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        paymentResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPaymentMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentMySuffix).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
