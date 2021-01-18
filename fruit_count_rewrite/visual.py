import torchvision.utils as vutils
from tensorboardX import SummaryWriter
import torchvision.transforms as standard_transforms
import torch
import numpy as np




def vis_results(exp_name, epoch, writer, restore, img, pred_map, gt_map):
    pil_to_tensor = standard_transforms.ToTensor()

    x = []

    for idx, tensor in enumerate(zip(img.cpu().data, pred_map, gt_map)):
        if idx > 1:  # show only one group
            break
        pil_input = restore(tensor[0])
        pil_output = torch.from_numpy(tensor[1] / (tensor[2].max() + 1e-10)).repeat(3, 1, 1)
        pil_label = torch.from_numpy(tensor[2] / (tensor[2].max() + 1e-10)).repeat(3, 1, 1)
        x.extend([pil_to_tensor(pil_input.convert('RGB')), pil_label, pil_output])
    x = torch.stack(x, 0)
    x = vutils.make_grid(x, nrow=3, padding=5)
    x = (x.numpy() * 255).astype(np.uint8)

    writer.add_image(exp_name + '_epoch_' + str(epoch + 1), x)