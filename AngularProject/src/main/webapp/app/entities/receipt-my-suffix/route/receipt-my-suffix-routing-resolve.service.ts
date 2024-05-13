import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReceiptMySuffix } from '../receipt-my-suffix.model';
import { ReceiptMySuffixService } from '../service/receipt-my-suffix.service';

const receiptResolve = (route: ActivatedRouteSnapshot): Observable<null | IReceiptMySuffix> => {
  const id = route.params['id'];
  if (id) {
    return inject(ReceiptMySuffixService)
      .find(id)
      .pipe(
        mergeMap((receipt: HttpResponse<IReceiptMySuffix>) => {
          if (receipt.body) {
            return of(receipt.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default receiptResolve;
