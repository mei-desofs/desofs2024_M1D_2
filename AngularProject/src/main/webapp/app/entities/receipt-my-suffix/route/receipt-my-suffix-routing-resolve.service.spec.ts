import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IReceiptMySuffix } from '../receipt-my-suffix.model';
import { ReceiptMySuffixService } from '../service/receipt-my-suffix.service';

import receiptResolve from './receipt-my-suffix-routing-resolve.service';

describe('ReceiptMySuffix routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ReceiptMySuffixService;
  let resultReceiptMySuffix: IReceiptMySuffix | null | undefined;

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
    service = TestBed.inject(ReceiptMySuffixService);
    resultReceiptMySuffix = undefined;
  });

  describe('resolve', () => {
    it('should return IReceiptMySuffix returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        receiptResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReceiptMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReceiptMySuffix).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        receiptResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReceiptMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReceiptMySuffix).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IReceiptMySuffix>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        receiptResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultReceiptMySuffix = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReceiptMySuffix).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
