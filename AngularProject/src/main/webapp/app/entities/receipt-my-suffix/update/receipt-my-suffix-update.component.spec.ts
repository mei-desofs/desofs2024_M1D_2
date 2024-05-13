import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ReceiptMySuffixService } from '../service/receipt-my-suffix.service';
import { IReceiptMySuffix } from '../receipt-my-suffix.model';
import { ReceiptMySuffixFormService } from './receipt-my-suffix-form.service';

import { ReceiptMySuffixUpdateComponent } from './receipt-my-suffix-update.component';

describe('ReceiptMySuffix Management Update Component', () => {
  let comp: ReceiptMySuffixUpdateComponent;
  let fixture: ComponentFixture<ReceiptMySuffixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let receiptFormService: ReceiptMySuffixFormService;
  let receiptService: ReceiptMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReceiptMySuffixUpdateComponent],
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
      .overrideTemplate(ReceiptMySuffixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReceiptMySuffixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    receiptFormService = TestBed.inject(ReceiptMySuffixFormService);
    receiptService = TestBed.inject(ReceiptMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const receipt: IReceiptMySuffix = { id: 456 };

      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      expect(comp.receipt).toEqual(receipt);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReceiptMySuffix>>();
      const receipt = { id: 123 };
      jest.spyOn(receiptFormService, 'getReceiptMySuffix').mockReturnValue(receipt);
      jest.spyOn(receiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: receipt }));
      saveSubject.complete();

      // THEN
      expect(receiptFormService.getReceiptMySuffix).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(receiptService.update).toHaveBeenCalledWith(expect.objectContaining(receipt));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReceiptMySuffix>>();
      const receipt = { id: 123 };
      jest.spyOn(receiptFormService, 'getReceiptMySuffix').mockReturnValue({ id: null });
      jest.spyOn(receiptService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receipt: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: receipt }));
      saveSubject.complete();

      // THEN
      expect(receiptFormService.getReceiptMySuffix).toHaveBeenCalled();
      expect(receiptService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReceiptMySuffix>>();
      const receipt = { id: 123 };
      jest.spyOn(receiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(receiptService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
